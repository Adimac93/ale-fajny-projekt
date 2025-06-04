package org.alefajnyprojekt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataLoggerTest {

    @TempDir
    Path tempDir;

    private DataLogger logger;
    private File logFile;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        if (logFile != null && logFile.exists()) {
            System.out.println("Deleting test log file: " + logFile.getAbsolutePath());
            logFile.delete(); // Usually @TempDir handles cleanup
        }
    }

    private File findLogFile(String directoryPath, String datePart) {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((d, name) -> name.startsWith(datePart) && name.endsWith(".csv"));
        if (files != null && files.length > 0) {
            // Assuming only one file matches for this test run in the temp dir
            return files[0];
        }
        return null;
    }

    @Test
    void constructorShouldCreateLogFileWithHeader() throws IOException {
        String path = tempDir.toString();
        logger = new DataLogger(path); // Logger creates file in constructor
        logger.saveResults(); // Close the writers to ensure content is flushed for reading

        logFile = findLogFile(path, LocalDate.now().toString());
        assertNotNull(logFile, "Log file should be created in the specified directory.");
        assertTrue(logFile.exists(), "Log file should exist.");
        assertTrue(logFile.length() > 0, "Log file should not be empty.");

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String header = reader.readLine();
            assertEquals("turnNumber,healthyCount,recoveredCount,infectedCount,deceasedCount", header,
                    "Log file should start with the correct CSV header.");
        }
    }

    @Test
    void defaultConstructorShouldCreateLogFileInCurrentDirectory() throws IOException {
        logger = new DataLogger(); // Creates file in "./"
        logger.saveResults();

        // This test is a bit more fragile as it depends on the current working directory
        // and finding the generated file.
        logFile = findLogFile("./", LocalDate.now().toString());
        assertNotNull(logFile, "Log file should be created in the current directory by default constructor.");
        assertTrue(logFile.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String header = reader.readLine();
            assertEquals("turnNumber,healthyCount,recoveredCount,infectedCount,deceasedCount", header);
        }
        // Clean up the file created in the current directory
        if (logFile != null && logFile.exists()) {
            logFile.delete();
        }
    }


    @Test
    void saveTurnStateShouldWriteCorrectData() throws IOException {
        String path = tempDir.toString();
        logger = new DataLogger(path);

        logger.saveTurnState(1, 10, 2, 3, 0);
        logger.saveTurnState(2, 8, 3, 2, 1);
        logger.saveResults(); // Important to save/close before reading

        logFile = findLogFile(path, LocalDate.now().toString());
        assertNotNull(logFile, "Log file should be found after writing data.");

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        assertEquals(3, lines.size(), "Log file should contain header and two data rows.");
        assertEquals("turnNumber,healthyCount,recoveredCount,infectedCount,deceasedCount", lines.get(0));
        assertEquals("1, 10, 2, 3, 0", lines.get(1).trim(), "First data row not as expected.");
        assertEquals("2, 8, 3, 2, 1", lines.get(2).trim(), "Second data row not as expected.");
    }
}