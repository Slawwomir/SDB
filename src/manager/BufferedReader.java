package manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public class BufferedReader {
    public static final int DOUBLE_SIZE = Double.SIZE / Byte.SIZE;
    public static final int RECORD_SIZE = 6 * DOUBLE_SIZE;         // 6 x double

    private int blockSize;                      // in bytes
    private FileInputStream in;
    private FileOutputStream out;
    private byte[] block;
    private byte[] storage;
    private int index;

    public BufferedReader(String filename, int blockSize) throws FileNotFoundException {
        this.blockSize = blockSize;
        block = new byte[blockSize];
        storage = new byte[RECORD_SIZE];
        in = new FileInputStream(filename);
        out = new FileOutputStream(filename);
    }

    public Double readDouble() {
        Double dd = null;

        if ((index + 1) * DOUBLE_SIZE < blockSize) {
            dd = ByteBuffer.wrap(block, DOUBLE_SIZE * index, DOUBLE_SIZE).getDouble();
            index++;
        } else {

        }

        return dd;
    }
}
