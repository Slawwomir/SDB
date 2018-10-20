package service.manager;

import service.buffer.BufferedBinaryWriter;
import service.model.Point;
import service.model.Record;

import java.io.FileNotFoundException;
import java.io.IOException;

public class RecordWriter {
    private BufferedBinaryWriter writer;

    public RecordWriter(String filename, int blockSize) throws FileNotFoundException {
        writer = new BufferedBinaryWriter(filename, blockSize);
    }

    public void push(Record record) throws IOException {
        for (Point point : record.getPoints()) {
            writer.saveDouble(point.x);
            writer.saveDouble(point.y);
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}
