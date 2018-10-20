package sort;

import service.manager.RecordReader;
import service.manager.RecordWriter;
import service.model.Record;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Converter {
    public static int blockSize = 4096;

    public static void txtToBinary(String txt, String binary) throws IOException {
        RecordWriter recordWriter = new RecordWriter(binary, blockSize);
        BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(txt));

        for (String line : bufferedReader.lines().collect(Collectors.toList())) {
            recordWriter.push(new Record(line));
        }

        bufferedReader.close();
        recordWriter.close();
    }

    public static void binaryToTxt(String binary, String txt) throws IOException {
        RecordReader recordReader = new RecordReader(binary, blockSize);
        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(txt));

        while (recordReader.hasNext()) {
            bufferedWriter.write(recordReader.next().toString());
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
        recordReader.close();
    }
}
