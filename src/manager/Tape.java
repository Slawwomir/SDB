package manager;

import model.Record;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tape {
    private RecordReader reader;
    private RecordWriter writer;
    private String filename;
    private Record previous;
    private int blockSize;
    private int dummies;
    private boolean empty;
    private boolean inRun;
    private int runsNumber;
    private boolean returned;

    public Tape(String filename, int blockSize) throws FileNotFoundException {
        this.filename = filename;
        this.blockSize = blockSize;

        reader = new RecordReader(filename, blockSize);
        writer = new RecordWriter(filename, blockSize);
    }

    public List<Record> getRecords(int amount) throws IOException {
        List<Record> records = new ArrayList<>(amount);

        for (int i = 0; i < amount; i++) {
            records.add(reader.next());
        }

        empty = !reader.hasNext();

        return records;
    }

    public Record next() throws IOException {
        if (returned) {
            returned = false;
            return previous;
        }

        if (reader.hasNext()) {
            Record record = reader.next();
            inRun = previous == null || record.compareTo(previous) >= 0;
            previous = record;

            return record;
        }

        return previous = null;
    }

    public boolean hasNext() throws IOException {
        if (returned)
            return previous != null;
        else
            return reader.hasNext();
    }

    public boolean isEmpty() {
        return previous == null && empty;
    }

    public void push(Record record) throws IOException {
        if (previous == null || record.compareTo(previous) < 0) {
            runsNumber++;
        }

        writer.push(record);
        previous = record;
    }

    public void flushOutput() throws IOException {
        writer.close();
    }

    public void flushInput() throws IOException {
        reader.close();
        previous = null;
        returned = false;
        reader = new RecordReader(filename, blockSize);
    }

    public int getRunsNumber() {
        return runsNumber;
    }

    public boolean isInRun() {
        return inRun;
    }

    public void setDummies(int dummies) {
        this.dummies = dummies;
    }

    public void print() throws IOException {
        flushInput();

        while (reader.hasNext()) {
            System.out.println(reader.next());
        }
    }

    public void openOutput() throws FileNotFoundException {
        writer = new RecordWriter(filename, blockSize);
    }

    public int getDummies() {
        return dummies;
    }

    public void restoreLast() {
        returned = true;
    }
}
