package sort;

public class Fibonacci {
    private int counter = 0;
    private int previous = 0;
    private int current = 1;

    public int next() {
        counter++;
        current += previous;
        previous = current - previous;

        return current;
    }

    public int previous() {
        counter--;
        previous = current - previous;
        current -= previous;

        return current;
    }

    public int get() {
        return current;
    }

    public int getCounter() {
        return counter;
    }
}
