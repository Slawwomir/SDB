package sort;

import service.model.Point;
import service.model.Record;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class DataFactory {

    private List<Record> data = new ArrayList<>();

    public void createRandomData(int recordsAmount) {
        data = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < recordsAmount; i++) {
            data.add(new Record(
                    new Point(rand.nextDouble() * recordsAmount, rand.nextDouble() * recordsAmount),
                    new Point(rand.nextDouble() * recordsAmount, rand.nextDouble() * recordsAmount),
                    new Point(rand.nextDouble() * recordsAmount, rand.nextDouble() * recordsAmount)
            ));
        }
    }

    public void createDataInRange(double from, double to, int recordsAmount) {
        data = new ArrayList<>();
        double step = (to - from) / recordsAmount;

        for (int i = 0; i < recordsAmount; i++, from += step) {
            data.add(new Record(
                    new Point(from, from),
                    new Point(from, from),
                    new Point(from, from)
            ));
        }
    }

    public void sortData() {
        Record[] records = data.toArray(new Record[]{});
        Arrays.sort(records);
        data = Arrays.asList(records);
    }

    public void sortDataReverse() {
        Record[] records = data.toArray(new Record[]{});
        Arrays.sort(records, Collections.reverseOrder());
        data = Arrays.asList(records);
    }

    public void saveToFile(String filename) throws IOException {
        File file = new File(filename);
        file.createNewFile();
        new PrintWriter(file).write("");

        Files.write(file.toPath(), data.stream().map(Record::toString).collect(Collectors.toList()));
    }

    public List<Record> getData() {
        return data;
    }
}
