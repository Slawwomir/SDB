package model;

import javax.print.DocFlavor;

public class Record implements Comparable<Record> {
    public static Record MIN = new Record(new Point(0, 0), new Point(0, 0), new Point(0, 0));

    private Point A;
    private Point B;
    private Point C;

    public Record(Point a, Point b, Point c) {
        A = a;
        B = b;
        C = c;
    }

    public double getArea() {
        return 0.5 * ((A.x - B.x) * (A.y - C.y) + (A.y - B.y) * (A.x - C.x));
        //return A.x;
    }

    public static Record min(Record r1, Record r2) {
        return r1.compareTo(r2) >= 0 ? r2 : r1;
    }

    public static Record max(Record r1, Record r2) {
        return r1.compareTo(r2) >= 0 ? r1 : r2;
    }

    public static int compare(Record r1, Record r2) {
        return r1.compareTo(r2);
    }

    @Override
    public int compareTo(Record record) {
        return Double.compare(getArea(), record.getArea());
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", A, B, C);
    }
}
