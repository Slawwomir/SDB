package UnitTests;

import org.junit.Test;
import service.manager.RecordReader;
import service.model.Record;
import sort.DataFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class TestDataFactory {

    @Test
    public void testWriteAndReadTxtFile() throws IOException {
        DataFactory factory = new DataFactory();
        factory.createRandomData(10000);
        factory.saveToTxtFile("testWriteAndReadTxtFile.txt");

        BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("testWriteAndReadTxtFile.txt"));

        for(Record record : factory.getData()) {
            assertTrue(record.equals(new Record(bufferedReader.readLine())));
        }
    }

    @Test
    public void testWriteAndReadBinaryFile() throws IOException {
        DataFactory factory = new DataFactory();
        factory.createRandomData(10000);
        factory.saveToBinaryFile("testWriteAndReadBinaryFile.bin");

        RecordReader recordReader = new RecordReader("testWriteAndReadBinaryFile.bin", 4096);

        for(Record record : factory.getData()) {
            assertTrue(record.equals(recordReader.next()));
        }
    }
}
