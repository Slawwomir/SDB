package buffer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BufferedWriter {
    public static final int DOUBLE_SIZE = Double.SIZE / Byte.SIZE;
    public static int diskOperationCounter;

    private int blockSize;                      // in bytes
    private byte[] block;
    private int index;
    private String filename;
    private FileOutputStream out;


    public BufferedWriter(String filename, int blockSize) throws FileNotFoundException {
        this.blockSize = blockSize;
        this.filename = filename;
        block = new byte[blockSize];
    }

    public void close() throws IOException {
        if(index > 0) {
            writeBlock();
            out.close();
        }
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
        if (out == null) {
            out = new FileOutputStream(filename);
        }

        out.write(block, 0, index);
        index = 0;
        diskOperationCounter++;
    }
}
