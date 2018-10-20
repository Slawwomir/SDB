package sort;

import service.manager.Tape;
import service.model.Record;
import service.manager.TapeManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class PolyphaseSort {
    public static final int blockSize = 4096;
    private List<Tape> tapes;
    private int phases;

    public PolyphaseSort(String filename) throws IOException {
        File tape1 = new File("T1" + filename);
        File tape2 = new File("T2" + filename);

        Files.deleteIfExists(tape1.toPath());
        Files.deleteIfExists(tape2.toPath());

        Files.createFile(tape1.toPath());
        Files.createFile(tape2.toPath());

        tapes = Arrays.asList(
                new Tape(filename, blockSize),
                new Tape("T1" + filename, blockSize),
                new Tape("T2" + filename, blockSize)
        );
    }

    public int sort() throws IOException {
        phases = distribute();

        for (int i = 0; i < phases; i++) {
            merge();
        }

        return tapes.get(1).getRunsNumber() + tapes.get(2).getRunsNumber();
    }

    public String getSortedFile() {
        return tapes.get(1).getFilename();
    }

    private int distribute() throws IOException {
        Fibonacci fibonacci = new Fibonacci();

        Tape source = tapes.get(0);
        Tape destination = tapes.get(1);

        while (source.hasNext()) {
            Record record = source.next();

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
        TapeManager manager = new TapeManager(tapes.get(1), tapes.get(2));

        while (manager.hasNext()) {
            manager.update();

            while (true) {
                Record current = manager.getMinimum();
                if (current == null)
                    break;
                tapes.get(0).push(current);
            }
        }

        Tape temp = tapes.get(0);
        temp.flushInput();
        temp.flushOutput();

        if (tapes.get(1).hasNext()) {
            tapes.set(0, tapes.get(2));
            tapes.set(2, temp);
        } else {
            tapes.set(0, tapes.get(1));
            tapes.set(1, temp);
        }
    }

    public void sortAndShowFiles() throws IOException {

        System.out.println("BEFORE DISTRIBUTION");

        for (Tape tape : tapes) {
            tape.print();
        }
        System.out.println("--------------------------------------------");


        int phaseNumber = distribute();

        System.out.println("\nAFTER DISTRIBUTION");

        System.out.println("TAPE: " + tapes.get(0).getFilename());
        tapes.get(1).print();
        tapes.get(2).print();

        System.out.println("--------------------------------------------");


        for (int i = 0; i < phaseNumber; i++) {
            System.out.println("\nMERGE " + i);
            merge();
            System.out.println("TAPE: " + tapes.get(0).getFilename());
            tapes.get(1).print();
            tapes.get(2).print();
            System.out.println("--------------------------------------------");
        }

        System.out.println("\nSORTED:");
        tapes.get(1).print();
    }

    public int getPhasesNumber() {
        return phases;
    }
}

