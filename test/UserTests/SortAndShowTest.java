package UserTests;

import org.junit.Test;
import sort.Converter;
import sort.DataFactory;
import sort.PolyphaseSort;

import java.io.IOException;

public class SortAndShowTest {

    @Test
    public void showEachOneStepRandomData() throws IOException {
        DataFactory dataFactory = new DataFactory();
        dataFactory.createRandomData(12);
        dataFactory.sortDataReverse();
        dataFactory.saveToBinaryFile("showEachOneStepRandomData.bin");

        PolyphaseSort polyphaseSort = new PolyphaseSort("showEachOneStepRandomData.bin");
        polyphaseSort.sortAndShowFiles();
    }

    @Test
    public void showEachOneStepUserData() throws IOException {

        try {
            Converter.txtToBinary("UserData.txt", "UserData.bin");
        } catch (IOException e) {
            throw new IOException("User doesn't provide UserData.txt\n" + e);
        }

        PolyphaseSort polyphaseSort = new PolyphaseSort("userData.bin");
        polyphaseSort.sortAndShowFiles();
    }
}
