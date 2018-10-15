import service.manager.DiskDriver;
import org.junit.Test;
import sort.DataFactory;
import sort.PolyphaseSortDeprecated;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PolyphaseSortTest {

    public static int RANGE = 1_000_000;
    private static DataFactory factory = new DataFactory();

    @Test
    public void sortRandomData() throws IOException {
        for (int i = 1; i <= RANGE; i *= 10) {
            factory.createRandomData(i);
            factory.saveToFile("testSort");

            // test
            factory.sortData();
            factory.saveToFile("sortedData" + i);

            sortTest(i, "testSort");
        }
    }

    @Test
    public void sortSortedData() throws IOException {
        for (int i = 1; i <= RANGE; i *= 10) {
            factory.createRandomData(i);
            factory.sortData();
            factory.saveToFile("testSort");

            sortTest(i, "testSort");
        }
    }

    @Test
    public void sortSortedReverseData() throws IOException {
        for (int i = 1; i <= RANGE; i *= 10) {
            factory.createRandomData(i);
            factory.sortDataReverse();
            factory.saveToFile("testSort");

            sortTest(i, "testSort");
        }
    }

    @Test
    public void sortAndShowFiles() throws IOException {
        System.out.println("\n Step by step for 100 random records");
        factory.createRandomData(100);
        factory.saveToFile("testSort");
        PolyphaseSortDeprecated polyphaseSort = new PolyphaseSortDeprecated("testSort");
        polyphaseSort.sortAndShowFiles();
    }

    private static void sortTest(int i, String filename) throws IOException {
        System.out.println("Unsorted file:");
        //showFile(new File(filename));

        System.out.println("Sort " + i + " records: ");
        PolyphaseSortDeprecated polyphaseSort = new PolyphaseSortDeprecated(filename);
        long time = System.currentTimeMillis();
        File sorted = polyphaseSort.sort();
        System.out.println("Time = " + (System.currentTimeMillis() - time) + " ms.");

        FileChannel inputChannel = null;
        FileChannel outputChannel = null;

        try {
            inputChannel = new FileInputStream(sorted).getChannel();
            outputChannel = new FileOutputStream(new File("output" + i)).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }


        System.out.println("Disk operations: " + DiskDriver.diskOperationCounter);
        System.out.println("Number of runs: " + polyphaseSort.getRunsNumber() + "\n");
        DiskDriver.diskOperationCounter = 0;

        System.out.println("Sorted file:");
        //showFile(sorted);
    }

    private static void showFile(File file) throws IOException {
        System.out.println("-------------------------------------------");

        BufferedReader b = Files.newBufferedReader(Paths.get(file.getName()));
        b.lines().forEach(System.out::println);
        System.out.println("-------------------------------------------\n");
    }

    public static void main(String... args) throws IOException {
        System.out.println("Insert number of records: ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int recordsNumber = Integer.parseInt(in.readLine());
        System.out.println("Write records in lines: ");

        FileWriter fileWriter = new FileWriter("out.txt");

        for (int i = 0; i < recordsNumber; i++) {
            fileWriter.write(in.readLine() + "\n");
        }

        fileWriter.close();
        System.out.println("OK!");

        new PolyphaseSortDeprecated("out.txt").sortAndShowFiles();
        // sortTest(recordsNumber, "out.txt");
    }
}