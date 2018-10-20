package service.manager;

import service.buffer.BufferedBinaryReader;
import service.model.Point;
import service.model.Record;

import java.io.FileNotFoundException;
import java.io.IOException;

public class RecordReader {
    private BufferedBinaryReader reader;

    public RecordReader(String filename, int blockSize) throws FileNotFoundException {
        reader = new BufferedBinaryReader(filename, blockSize);
    }

    public RecordReader(String filename) throws FileNotFoundException {
        this(filename, 4096);
    }

    public Record next() throws IOException {
        if (!reader.isEmpty()) {
            return new Record(
                    new Point(reader.readDouble(),
                            reader.readDouble()),
                    new Point(reader.readDouble(),
                            reader.readDouble()),
                    new Point(reader.readDouble(),
                            reader.readDouble())
            );
        }

        return null;
    }

    public boolean hasNext() {
        return !reader.isEmpty();
    }

    public void close() throws IOException {
        reader.close();
    }
}
