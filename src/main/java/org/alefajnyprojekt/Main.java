package org.alefajnyprojekt;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Config config = new ObjectMapper().readValue(new File("config.json"), Config.class);
                Simulation simulation = new Simulation(
                        config.boardWidth, config.boardHeight,
                        config.numHumans, config.numRats, config.numPets,
                        config.initiallyInfected, config.maxTurns
                );
            } catch (Exception e) {
                System.err.println("Error loading configuration: " + e.getMessage());
            }
        });
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