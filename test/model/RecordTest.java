package model;

import static org.junit.Assert.*;

public class RecordTest {
    private Record r1 = new Record(new Point(0, 0), new Point(4,0), new Point(0,4));
    private Record r2 = new Record(new Point(0, 0), new Point(5,0), new Point(0,5));
    private Record r3 = new Record(new Point(0, 0), new Point(3, 3), new Point(6,0));

    @org.junit.Test
    public void compare() {
        assertEquals(Record.compare(r1, r2), -1);
        assertEquals(Record.compare(r2, r1), 1);
        assertEquals(Record.compare(r1, r3), -1);
        assertEquals(Record.compare(r2, r3), 1);
        assertEquals(Record.compare(r1, r1), 0);
    }

    @org.junit.Test
    public void getArea() {
        assertEquals(r1.getArea(), 8, 0.1);
        assertEquals(r2.getArea(), 12.5, 0.1);
        assertEquals(r3.getArea(), 9, 0.1);
    }
}