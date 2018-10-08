import manager.DiskDriver;
import manager.TapeManager;
import model.Record;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PolyphaseSort {
    public static final int TAPES_NUMBER = 3;
    public static final int BLOCK_SIZE = 100;

    private int runsNumber;

    private List<TapeManager> tapes;

    public PolyphaseSort(String source) throws IOException {
        tapes = Arrays.asList(new TapeManager(new DiskDriver(BLOCK_SIZE, source)), new TapeManager(new DiskDriver(BLOCK_SIZE, "tape1")), new TapeManager(new DiskDriver(BLOCK_SIZE, "tape2")));
        tapes.get(1).clear();
        tapes.get(2).clear();
    }

    public File sort() throws IOException {
        int phasesNumber = distribute();
        System.out.println("Phases: " + phasesNumber);
        for (int i = 0; i < phasesNumber; i++)
            merge();

        return tapes.get(1).getFile();
    }

    public void sortAndShowFiles() throws IOException {
        System.out.println("\n\n----- Before distribution -----");
        showFiles();

        int phasesNumber = distribute();

        System.out.println("\n\n----- After distribution -----");
        showFiles();

        System.out.println("\n\n -------- Merging --------");
        for (int i = 0; i < phasesNumber; i++) {
            merge();
            System.out.println("\n\n--- Merge " + i + " phase ---");
            showFiles();
        }
    }

    private void showFiles() throws IOException {
        for (TapeManager tape : tapes) {
            System.out.println("\n" + tape.getFile());
            if (tape.getPointer() != tape.getDataSize())
                System.out.println("Point to " + tape.getPointer() + " record");
            if (tape.getDummiesNumber() > 0)
                System.out.println("Has " + tape.getDummiesNumber() + " dummies");
            showFile(tape.getFile(), tape.getPointer() != tape.getDataSize() ? tape.getPointer() : 0);
        }
    }

    private void showFile(File file, int pointer) throws IOException {
        System.out.println("-------------------------------------------");

        BufferedReader b = Files.newBufferedReader(Paths.get(file.getName()));
        int i = 0;
        for (String line : b.lines().collect(Collectors.toList())) {
            if (i >= pointer)
                System.out.println("[" + i + "] " + line);
            i++;
        }

        System.out.println("-------------------------------------------\n");
    }

    private int distribute() throws IOException {
        TapeManager destination = tapes.get(1);
        Fibonacci fibonacci = new Fibonacci();

        while (tapes.get(0).hasNext()) {
            if (!tapes.get(0).hasNextInRun() && destination.getRunsNumber() >= fibonacci.get()) {
                destination = (destination == tapes.get(1) ? tapes.get(2) : tapes.get(1));
                fibonacci.next();
            }

            destination.put(tapes.get(0).next());
        }

        destination.setDummiesNumber(fibonacci.get() - destination.getRunsNumber());

        for (TapeManager tape : tapes) {
            tape.restore();
        }

        runsNumber = tapes.get(1).getRunsNumber() + tapes.get(0).getRunsNumber();

        return fibonacci.getCounter();
    }

    private void merge() throws IOException {

        tapes.get(0).clear();
        RecordManager manager = new RecordManager(tapes.get(1), tapes.get(2));

        while (tapes.get(1).hasNext() && tapes.get(2).hasNext()) {
            manager.update();
            Record current = null;

            do {
                current = manager.getMinimum();
                if (current != null)
                    tapes.get(0).put(current);
                else
                    break;
            } while (true);
        }

        for (TapeManager tape : tapes) {
            tape.restore();
        }

        TapeManager temp = tapes.get(0);

        if (tapes.get(1).hasNext()) {
            tapes.set(0, tapes.get(2));
            tapes.set(2, temp);
        } else {
            tapes.set(0, tapes.get(1));
            tapes.set(1, temp);
        }

        tapes.get(0).clear();
    }

    public int getRunsNumber() {
        return runsNumber;
    }

    private class Fibonacci {
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

    private class RecordManager {
        TapeManager tp1;
        TapeManager tp2;
        Record r1;
        Record r2;

        public RecordManager(TapeManager tp1, TapeManager tp2) {
            this.tp1 = tp1;
            this.tp2 = tp2;
        }

        public void update() throws IOException {
            if (tp1.getDummiesNumber() > 0) {
                r1 = null;
                tp1.setDummiesNumber(tp1.getDummiesNumber() - 1);
            } else {
                r1 = tp1.next();
            }

            if (tp2.getDummiesNumber() > 0) {
                r2 = null;
                tp2.setDummiesNumber(tp2.getDummiesNumber() - 1);
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
                if (tp2.hasNextInRun())
                    r2 = tp2.next();
                else
                    r2 = null;
            } else {
                minimum = r1;
                if (tp1.hasNextInRun())
                    r1 = tp1.next();
                else
                    r1 = null;
            }
            return minimum;
        }
    }
}
