package UnitTests.sort;

import org.junit.Test;
import service.manager.RecordReader;
import service.model.Record;
import sort.Converter;
import sort.DataFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class ConverterTest {

    @Test
    public void txtToBinary() throws IOException {
        DataFactory dataFactory = new DataFactory();
        dataFactory.createRandomData(10_000);
        dataFactory.saveToTxtFile("txtToBinary.txt");

        Converter.txtToBinary("txtToBinary.txt", "txtToBinary.bin");

        RecordReader recordReader = new RecordReader("txtToBinary.bin");
        for (Record record : dataFactory.getData()) {
            assertTrue(record.equals(recordReader.next()));
        }
    }

    @Test
    public void binaryToTxt() throws IOException {
        DataFactory dataFactory = new DataFactory();
        dataFactory.createRandomData(10_000);
        dataFactory.saveToBinaryFile("binaryToTxt.bin");

        Converter.binaryToTxt("binaryToTxt.bin", "binaryToTxt.txt");

        BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("binaryToTxt.txt"));
        for (Record record : dataFactory.getData()) {
            assertTrue(record.equals(new Record(bufferedReader.readLine())));
        }
    }
}