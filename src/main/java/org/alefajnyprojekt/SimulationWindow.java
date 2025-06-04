package org.alefajnyprojekt;

import org.alefajnyprojekt.entities.Entity;
import org.alefajnyprojekt.enums.HealthStatus;
import javax.swing.*;
import java.awt.*;

public class SimulationWindow {
    private final Simulation simulation;
    private JFrame frame;
    private JTextArea textArea;

    public SimulationWindow(Simulation simulation) {
        this.simulation = simulation;
        initializeWindow();
    }

    /**
     * Initializes the main window for the simulation.
     * Sets up the JFrame, JTextArea, and JScrollPane for displaying the simulation state.
     */
    private void initializeWindow() {
        frame = new JFrame("Epidemic Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * Updates the display with the current state of the simulation.
     * Generates the board visualization and statistics, then updates the JTextArea.
     */
    public void updateDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Turn ").append(simulation.getCurrentTurn()).append(" ===\n\n");
        sb.append(generateBoardString());
        sb.append("\n").append(generateStatsString());

        textArea.setText(sb.toString());
        textArea.setCaretPosition(0);
    }


    /**
     * Generates the board visualization string.
     * @return String representing the current board state
     */
    private String generateBoardString() {
        StringBuilder sb = new StringBuilder();
        Board board = simulation.getBoard();

        // Top border
        sb.append("+").append("-".repeat(board.getWidth() * 2)).append("+\n");

        // Board content
        for (int y = 0; y < board.getHeight(); y++) {
            sb.append("|");
            for (int x = 0; x < board.getWidth(); x++) {
                sb.append(getSymbolForPosition(new Position(x, y)));
            }
            sb.append("|\n");
        }

        // Bottom border
        sb.append("+").append("-".repeat(board.getWidth() * 2)).append("+\n");

        return sb.toString();
    }

    /**
     * Gets the symbol for entity at given position.
     * @param pos Position to check
     * @return String representation of the entity
     */
    private String getSymbolForPosition(Position pos) {
        for (Entity entity : simulation.getEntityList()) {
            if (entity.getPosition().equals(pos)) {
                switch (entity.getHealthStatus()) {
                    case HEALTHY:
                        switch (entity.getEntityType()) {
                            case "Human": return "👤";
                            case "Rat": return "🐀";
                            case "Pet": return "🐕";
                        }
                        break;
                    case INFECTED:
                        return "🤢";
                    case RECOVERED:
                        switch (entity.getEntityType()) {
                            case "Human": return "🦸";
                            case "Rat": return "\uD83D\uDC01";
                            case "Pet": return "\uD83D\uDC29";
                        }
                        break;
                    case DECEASED:
                        return "💀";
                }
            }
        }
        return "  "; // Empty space
    }

    /**
     * Generates statistics string.
     * @return Formatted statistics
     */
    private String generateStatsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Statistics:\n");

        long healthy = simulation.getEntityList().stream()
                .filter(e -> e.getHealthStatus() == HealthStatus.HEALTHY).count();
        long infected = simulation.getEntityList().stream()
                .filter(e -> e.getHealthStatus() == HealthStatus.INFECTED).count();
        long recovered = simulation.getEntityList().stream()
                .filter(e -> e.getHealthStatus() == HealthStatus.RECOVERED).count();
        long deceased = simulation.getEntityList().stream()
                .filter(e -> e.getHealthStatus() == HealthStatus.DECEASED).count();

        sb.append("  Healthy: ").append(healthy).append("\n");
        sb.append("  Infected: ").append(infected).append("\n");
        sb.append("  Recovered: ").append(recovered).append("\n");
        sb.append("  Deceased: ").append(deceased).append("\n");
        sb.append("  Total alive: ").append(healthy + infected + recovered).append("\n");

        return sb.toString();
    }

    /**
     * Shows final statistics dialog.
     * @param stats Statistics to display
     */
    public void showFinalStatistics(String stats) {
        textArea.setText(stats);
        JOptionPane.showMessageDialog(frame,
                "Simulation finished after " + simulation.getCurrentTurn() + " turns!",
                "Simulation Complete",
                JOptionPane.INFORMATION_MESSAGE);
    }
}