package buffer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BufferedReader {
    public static final int DOUBLE_SIZE = Double.SIZE / Byte.SIZE;
    public static int diskOperationCounter;

    private int blockSize;                      // in bytes
    private byte[] block;
    private int index;
    private int read;
    private boolean EOF;
    private boolean empty;
    private FileInputStream in;

    public BufferedReader(String filename, int blockSize) throws FileNotFoundException {
        this.blockSize = blockSize;
        block = new byte[blockSize];
        in = new FileInputStream(filename);
    }

    public void close() throws IOException {
        EOF = false;
        index = 0;
        read = 0;
        in.close();
    }

    public Double readDouble() throws IOException {
        if (empty = (EOF && index >= read)) {
            return null;
        }

        Double dd = null;

        if (index + DOUBLE_SIZE <= read) {
            dd = ByteBuffer.wrap(block, index, DOUBLE_SIZE).getDouble();
            index += DOUBLE_SIZE;
        } else {
            byte[] db = new byte[DOUBLE_SIZE];
            int offset = read - index;
            System.arraycopy(block, index, db, 0, offset);

            nextBlock();

            System.arraycopy(block, 0, db, offset, DOUBLE_SIZE - offset);
            index = DOUBLE_SIZE - offset;
            dd = ByteBuffer.wrap(db).getDouble();
        }

        return dd;
    }

    private void nextBlock() throws IOException {
        read = in.read(block, 0, blockSize);

        if (read < blockSize) {
            EOF = true;
        }

        index = 0;
        diskOperationCounter++;
    }

    public boolean isEmpty() {
        return empty = EOF && index >= read;
    }
}
