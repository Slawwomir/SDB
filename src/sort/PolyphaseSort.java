package sort;

import manager.Tape;
import model.Record;
import manager.RecordManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PolyphaseSort {
    public static final int blockSize = 4096;
    private List<Tape> tapes;

    public PolyphaseSort(String filename) throws IOException {
        new File(filename + "dst1").createNewFile();
        new File(filename + "dst2").createNewFile();

        tapes = Arrays.asList(
                new Tape(filename, blockSize),
                new Tape(filename + "dst1", blockSize),
                new Tape(filename + "dst2", blockSize)
        );
    }

    public void sort() throws IOException {
        int phaseNumber = distribute();

        for(int i = 0; i < phaseNumber; i++) {
            merge();
        }

        System.out.println("0. Tape");
        tapes.get(0).print();
        System.out.println("1. Tape");
        tapes.get(1).print();
        System.out.println("2. Tape");
        tapes.get(2).print();
    }

    private int distribute() throws IOException {
        Fibonacci fibonacci = new Fibonacci();

        Tape source = tapes.get(0);
        Tape destination = tapes.get(1);

        Record record;

        while (source.hasNext()) {
            record = source.next();

            if (destination.getRunsNumber() >= fibonacci.get() && !source.isInRun()) {
                destination = destination == tapes.get(1) ? tapes.get(2) : tapes.get(1);
                fibonacci.next();
            }
            destination.push(record);
        }

        destination.setDummies(fibonacci.get() - destination.getRunsNumber());

        for (Tape tape : tapes) {
            tape.flushOutput();
        }

        return fibonacci.getCounter();
    }

    private void merge() throws IOException {

        tapes.get(0).openOutput();
        RecordManager manager = new RecordManager(tapes.get(1), tapes.get(2));

        while (tapes.get(1).hasNext() && tapes.get(2).hasNext()) {
            manager.update();
            Record current = null;

            do {
                current = manager.getMinimum();
                if (current != null)
                    tapes.get(0).push(current);
                else
                    break;
            } while (true);
        }

        Tape temp = tapes.get(0);
        temp.flushOutput();
        temp.flushInput();

        if (tapes.get(1).hasNext()) {
            tapes.set(0, tapes.get(2));
            tapes.set(2, temp);
        } else {
            tapes.set(0, tapes.get(1));
            tapes.set(1, temp);
        }
    }
}

