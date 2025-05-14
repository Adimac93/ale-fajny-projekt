package org.alefajnyprojekt;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing disease spread simulator...");

        try {
            // Get initials parameters from JSON file
            Config config = new ObjectMapper().readValue(new File("config.json"), Config.class);

            // Create and run the simulation
            Simulation simulation = new Simulation(
                    config.boardWidth, config.boardHeight,
                    config.numHumans, config.numRats, config.numPets,
                    config.initiallyInfected, config.maxTurns
            );

            simulation.start();
        } catch (Exception e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            return;
        }

        System.out.println("Program execution finished.");
    }

    static class Config {
        public int boardWidth;
        public int boardHeight;
        public int numHumans;
        public int numRats;
        public int numPets;
        public int initiallyInfected;
        public int maxTurns;
    }
}