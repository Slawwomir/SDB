package manager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BufferedWriter {
    public static final int DOUBLE_SIZE = Double.SIZE / Byte.SIZE;

    private int blockSize;                      // in bytes
    private byte[] block;
    private int index;
    private int diskOperationCounter;

    private FileOutputStream out;

    public BufferedWriter(String filename, int blockSize) throws FileNotFoundException {
        this.blockSize = blockSize;
        block = new byte[blockSize];
        out = new FileOutputStream(filename);
    }

    public void close() throws IOException {
        writeBlock();
        out.close();
    }

    public void saveDouble(Double db) throws IOException {
        byte[] dd = new byte[DOUBLE_SIZE];
        ByteBuffer.wrap(dd).putDouble(db);

        if (index + DOUBLE_SIZE < blockSize) {
            System.arraycopy(dd, 0, block, index, DOUBLE_SIZE);
            index += DOUBLE_SIZE;
        } else {
            int offset = blockSize - index;
            System.arraycopy(dd, 0, block, index, offset);
            index = blockSize;

            writeBlock();

            index = DOUBLE_SIZE - offset;
            System.arraycopy(dd, offset, block, 0, index);
        }
    }

    private void writeBlock() throws IOException {
        out.write(block, 0, index);
        index = 0;
        diskOperationCounter++;
    }
}
