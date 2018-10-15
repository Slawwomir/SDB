package service.manager;

import service.buffer.BufferedReader;
import service.model.Point;
import service.model.Record;

import java.io.FileNotFoundException;
import java.io.IOException;

public class RecordReader {
    private BufferedReader reader;

    public RecordReader(String filename, int blockSize) throws FileNotFoundException {
        reader = new BufferedReader(filename, blockSize);
    }

    public Record next() throws IOException {
        if (!reader.isEmpty()) {
            return new Record(
                    new Point(reader.readDouble(), reader.readDouble()),
                    new Point(reader.readDouble(), reader.readDouble()),
                    new Point(reader.readDouble(), reader.readDouble())
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
