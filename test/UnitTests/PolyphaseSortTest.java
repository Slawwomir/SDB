package UnitTests;

import org.junit.Test;
import service.manager.RecordReader;
import service.model.Record;
import sort.DataFactory;
import sort.PolyphaseSort;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PolyphaseSortTest {

    @Test
    public void testSort() throws IOException {
        DataFactory dataFactory = new DataFactory();
        dataFactory.createRandomData(10_000);
        dataFactory.saveToBinaryFile("PolyphaseTestSort.bin");

        testDataSort(dataFactory, "PolyphaseTestSort.bin");
    }

    @Test
    public void testOneRunSort() throws IOException {
        DataFactory dataFactory = new DataFactory();
        dataFactory.createRandomData(10_000);
        dataFactory.sortData();
        dataFactory.saveToBinaryFile("PolyphaseTestSortOneRun.bin");

        testDataSort(dataFactory, "PolyphaseTestSortOneRun.bin");
    }

    @Test
    public void testEmptyFileSort() throws IOException {
        DataFactory dataFactory = new DataFactory();
        dataFactory.createRandomData(0);
        new File("PolyphaseTestSortEmpty.bin").createNewFile();

        testDataSort(dataFactory, "PolyphaseTestSortEmpty.bin");
    }

    @Test
    public void testDescSortedData() throws IOException {
        DataFactory dataFactory = new DataFactory();
        dataFactory.createRandomData(10_000);
        dataFactory.sortDataReverse();
        dataFactory.saveToBinaryFile("PolyphaseTestSortDesc.bin");

        testDataSort(dataFactory, "PolyphaseTestSortDesc.bin");
    }

    private void testDataSort(DataFactory dataFactory, String filename) throws IOException {
        PolyphaseSort polyphaseSort = new PolyphaseSort(filename);

        polyphaseSort.sort();
        String sortedFile = polyphaseSort.getSortedFile();
        dataFactory.sortData();

        RecordReader recordReader = new RecordReader(sortedFile);

        for (Record record : dataFactory.getData()) {
            assertEquals(record, recordReader.next());
        }
    }
}