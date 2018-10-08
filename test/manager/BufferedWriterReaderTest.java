package manager;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class BufferedWriterReaderTest {

    File file;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    public BufferedWriterReaderTest() throws IOException {
        String fileName = "test" + System.currentTimeMillis();
        file = new File(fileName);

        if(file.createNewFile()) {
            bufferedReader = new BufferedReader(fileName, 100);
            bufferedWriter = new BufferedWriter(fileName, 100);
        } else {
            throw new IOException("Utworzenie pliku zakończone niepowodzeniem");
        }
    }

    @Test
    public void testWriteReadDoublesToFile() throws IOException {
        Random random = new Random();
        double[] dbl = random.doubles(100000000).toArray();

        for(double d : dbl) {
            bufferedWriter.saveDouble(d);
        }
        bufferedWriter.close();

        for(double d : dbl) {
            Assert.assertEquals(bufferedReader.readDouble(), d, 1e-10);
        }
    }
}