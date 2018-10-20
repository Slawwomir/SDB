package PerformanceTests;

import sort.DataFactory;
import sort.PolyphaseSort;
import org.junit.Test;
import service.buffer.BufferedBinaryReader;
import service.buffer.BufferedBinaryWriter;

import java.io.IOException;

public class PolyphaseSortPerformanceTest {
    public static final int testLimit = 1_000_000;

    @Test
    public void randomDataTest() throws IOException {
        DataFactory dataFactory = new DataFactory();

        System.out.println("====> START PERFORMANCE TEST");


        for (int i = 1; i <= testLimit; i *= 10) {
            dataFactory.createRandomData(i);
            dataFactory.saveToBinaryFile("randomDataPerformanceTest" + i + ".bin");

            PolyphaseSort polyphaseSort = new PolyphaseSort("randomDataPerformanceTest" + i + ".bin");

            BufferedBinaryReader.diskOperationCounter = 0;
            BufferedBinaryWriter.diskOperationCounter = 0;

            long startTime = System.currentTimeMillis();
            int runsAmount = polyphaseSort.sort();
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("DATA SIZE: " + i + " records");
            System.out.println("TIME: " + duration + " ms");
            System.out.println("PHASES: " + polyphaseSort.getPhasesNumber());
            System.out.println("DISKS OPERATIONS: " + (BufferedBinaryWriter.diskOperationCounter + BufferedBinaryReader.diskOperationCounter));
            System.out.println("RUNS: " + runsAmount);
            System.out.println("---------------------------------");
        }
    }
}
