package manager;

import model.Record;
import model.Point;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class DiskDriver {
    private int blockSize;  //The number of records received in one access
    private int pointer;
    public static int diskOperationCounter;
    private Scanner scanner;
    private java.io.BufferedReader reader;
    private List<Record> readBlock;
    private List<Record> writeBlock;
    private File source;
    private boolean EOF;

    public DiskDriver(int blockSize, String fileName) throws IOException {
        this.blockSize = blockSize;
        this.source = new File(fileName);
        if (!source.exists())
            source.createNewFile();
        this.pointer = 0;
        this.readBlock = new ArrayList<>(blockSize);
        this.writeBlock = new ArrayList<>(blockSize);
        this.scanner = new Scanner(source);
        scanner.useLocale(Locale.US);
        reader = new java.io.BufferedReader(new FileReader(source));
    }

    public Record next() throws IOException {
        if (pointer < readBlock.size()) {
            return readBlock.get(pointer++);
        } else if (!EOF) {
            readNextBlock();
            return next();
        }
        return null;
    }

    private boolean readNextBlock() throws IOException {
        diskOperationCounter++;
        readBlock.clear();
        pointer = 0;
        String line;

        for (int i = 0; i < blockSize; i++) {
            line = reader.readLine();
            if (line != null) {
                String[] doubles = line.split(" ");
                readBlock.add(new Record(
                        new Point(Double.parseDouble(doubles[0]), Double.parseDouble(doubles[1])),
                        new Point(Double.parseDouble(doubles[2]), Double.parseDouble(doubles[3])),
                        new Point(Double.parseDouble(doubles[4]), Double.parseDouble(doubles[5]))
                ));
                EOF = false;
            } else {
                EOF = true;
                break;
            }
        }
        return true;
    }

    public void write(Record record) throws IOException {
        if (writeBlock.size() < blockSize) {
            writeBlock.add(record);
        } else {
            saveBlock();
            write(record);
        }
    }

    private void saveBlock() throws IOException {
        if (!writeBlock.isEmpty()) {
            diskOperationCounter++;
            Files.write(source.toPath(), writeBlock.stream().map(Record::toString).collect(Collectors.toList()), StandardOpenOption.APPEND);
            writeBlock.clear();
            EOF = false;
        }
    }

    public boolean hasNext() throws IOException {
        if (readBlock.isEmpty() || pointer == readBlock.size())
            if (!EOF)
                readNextBlock();
        return pointer != readBlock.size() || !EOF;
    }

    Record previous() {
        if (pointer > 0) {
            pointer--;
            return readBlock.get(pointer);
        }

        return null;
    }

    public void restore() throws IOException {
        saveBlock();
        //readBlock.clear();
        //scanner.close();
    }

    public void clearFile() throws IOException {
        new PrintWriter(source).write("");
        reader = new java.io.BufferedReader(new FileReader(source));
        scanner = new Scanner(source);
        scanner.useLocale(Locale.US);
    }

    public File getFile() {
        return source;
    }

    public int getPointer() {
        return pointer;
    }

    public int getDataSize() {
        return readBlock.size();
    }
}
