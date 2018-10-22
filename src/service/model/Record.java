package service.model;

import java.util.List;
import java.util.Objects;

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

    public Record(String line) {
        String[] doubles = line.split(" ");

        A = new Point(Double.parseDouble(doubles[0]), Double.parseDouble(doubles[1]));
        B = new Point(Double.parseDouble(doubles[2]), Double.parseDouble(doubles[3]));
        C = new Point(Double.parseDouble(doubles[4]), Double.parseDouble(doubles[5]));
    }

    public double getArea() {
        return 0.5 * Math.abs(A.x * (B.y - C.y) + B.x * (C.y - A.y) + C.x * (A.y - B.y));
    }

    //316043639

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return record.getArea() == getArea();
    }

    @Override
    public int hashCode() {
        return Objects.hash(A, B, C);
    }

    public List<Point> getPoints() {
        return List.of(A, B, C);
    }
}
