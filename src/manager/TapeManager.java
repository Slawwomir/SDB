package manager;

import model.Record;

import java.io.File;
import java.io.IOException;

public class TapeManager {
    private DiskDriver diskDriver;
    private Record previous;
    private boolean inRun;
    private int runsNumber;
    private int dummiesNumber;

    public TapeManager(DiskDriver diskDriver) {
        this.diskDriver = diskDriver;
    }

    public Record next() throws IOException {
        if (diskDriver.hasNext()) {
            Record record = diskDriver.next();
            inRun = previous == null || record.compareTo(previous) >= 0;
            previous = record;

            return record;
        }
        return null;
    }

    public boolean hasNext() throws IOException {
        return diskDriver.hasNext();
    }

    public boolean hasNextInRun() throws IOException {
        if (previous == null)
            return true;

        if (diskDriver.hasNext()) {
            boolean value = diskDriver.next().compareTo(previous) >= 0;
            diskDriver.previous();
            return value;
        }

        return false;
    }

    public boolean isInRun() {
        return inRun;
    }

    public void setInRun(boolean inRun) {
        this.inRun = inRun;
    }

    public Record getPrevious() {
        diskDriver.previous();
        return previous;
    }

    public void put(Record record) {

        if (previous == null || record.compareTo(previous) < 0) {
            runsNumber++;
        }

        try {
            diskDriver.write(record);
            previous = record;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restore() throws IOException {
        previous = null;
        diskDriver.restore();
    }

    public int getRunsNumber() {
        return runsNumber;
    }

    public void setRunsNumber(int runsNumber) {
        this.runsNumber = runsNumber;
    }

    public int getDummiesNumber() {
        return dummiesNumber;
    }

    public void setDummiesNumber(int dummiesNumber) {
        this.dummiesNumber = dummiesNumber;
    }

    public void putInOrder(Record r1, Record r2) {
        if (r1.compareTo(r2) >= 0) {
            put(r2);
            put(r1);
        } else {
            put(r1);
            put(r2);
        }
    }

    public void clear() throws IOException {
        previous = null;
        diskDriver.clearFile();
    }

    public File getFile() {
        return diskDriver.getFile();
    }

    public int getPointer() {
        return diskDriver.getPointer();
    }

    public int getDataSize() {
        return diskDriver.getDataSize();
    }
}
