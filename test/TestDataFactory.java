import sort.DataFactory;

import java.io.IOException;

public class TestDataFactory {

    public static void main(String... args){
        DataFactory factory = new DataFactory();
        factory.createDataInRange(0, 1_000_000, 1_000_000);
        factory.sortDataReverse();

        try {
            factory.saveToFile("datafactory.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
