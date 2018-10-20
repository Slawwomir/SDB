package service.model;

import java.util.Locale;
import java.util.Objects;

public class Point {
    public static int round = 10;
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = Math.round(round*x)/round;
        this.y = Math.round(round*y)/round;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 &&
                Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%.1f %.1f", x, y);
    }
}
