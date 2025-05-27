package org.alefajnyprojekt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class DataLogger {
    private final File file;
    private FileWriter writer;
    private BufferedWriter bufferedWriter;

    public DataLogger() {
        this("");
    }

    public DataLogger(String path) {
        String filename = (path.isEmpty() ? "./" : path + "/") + LocalDate.now() + "-" + (int)(Math.random() * 101) +".csv";
        System.out.println(filename);
        file = new File(filename);
        try {
            if(file.createNewFile()) System.out.printf("Created new file at %s", file.getPath());
            writer = new FileWriter(file);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("turnNumber, healthyCount, recoveredCount, infectedCount, deceasedCount\n");
        } catch (IOException e) {
            System.out.println("ERROR: Could not initiate data logger module");
            System.exit(2);
        }
    }

    /**
     * Method creating a record in a csv file
     * @param turnNumber Field representing a current turn
     * @param healthyCount Field representing the count of healthy entities in a turn
     * @param recoveredCount Field representing the count of healthy entities in a turn
     * @param infectedCount Field representing the count of infected entities in a turn
     * @param deceasedCount Field representing the count of deceased entities in a turn
     */
    public void saveTurnState(long turnNumber, long healthyCount, long recoveredCount,
                              long infectedCount, long deceasedCount) {
        String payload = turnNumber + ", " + healthyCount + ", " +  recoveredCount + ", " + infectedCount + ", " + deceasedCount + "\n";
        try {
            bufferedWriter.write(payload);
        } catch (IOException e) {
            System.out.printf("Failed to log statistics of the turn %dl\n", turnNumber);
        }
    }

    /**
     * Closing writers and saving all buffered records
     */
    public void saveResults() {
        try {
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to save log data");
        }
    }
}
