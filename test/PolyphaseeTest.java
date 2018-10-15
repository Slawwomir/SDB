import org.junit.Test;
import sort.PolyphaseSort;

import java.io.IOException;

public class PolyphaseeTest {

    @Test
    public void testSort() throws IOException {
        new PolyphaseSort("test.txt").sort();
    }
}