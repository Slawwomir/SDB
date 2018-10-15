package manager;

import model.Record;

import java.io.IOException;

public class RecordManager {
    private Tape tp1;
    private Tape tp2;
    private Record r1;
    private Record r2;

    public RecordManager(Tape tp1, Tape tp2) {
        this.tp1 = tp1;
        this.tp2 = tp2;
    }

    public void update() throws IOException {
        if (tp1.getDummies() > 0) {
            r1 = null;
            tp1.setDummies(tp1.getDummies() - 1);
        } else {
            r1 = tp1.next();
        }

        if (tp2.getDummies() > 0) {
            r2 = null;
            tp2.setDummies(tp2.getDummies() - 1);
        } else {
            r2 = tp2.next();
        }
    }

    public Record getMinimum() throws IOException {
        Record minimum;

        if (r1 == null && r2 == null) {
            return null;
        }

        if ((r1 == null) || r2 != null && r1.compareTo(r2) > 0) {
            minimum = r2;
            r2 = tp2.next();
            if (!tp2.isInRun()) {
                r2 = null;
                tp2.restoreLast();
            }
        } else {
            minimum = r1;
            r1 = tp1.next();
            if (!tp1.isInRun()) {
                r1 = null;
                tp1.restoreLast();
            }
        }
        return minimum;
    }
}