package model;

import java.util.Locale;

public class Point {
    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%.1f %.1f", x, y);
    }
}