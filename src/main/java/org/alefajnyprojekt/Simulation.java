package org.alefajnyprojekt;

import org.alefajnyprojekt.entities.Entity;
import org.alefajnyprojekt.enums.HealthStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;

public class Simulation {
    private final Board board;
    private final List<Entity> entityList;
    private final EntityFactory entityFactory;
    private int currentTurn;
    private final int maxTurns;
    private DataLogger logger;
    private SimulationWindow window;
    private boolean running = true;

    /**
     * Constructs a new Simulation instance.
     * Initializes the board, entity factory, and entity list.
     * Sets up the simulation window and starts the simulation in a separate thread.
     *
     * @param boardWidth Width of the simulation board
     * @param boardHeight Height of the simulation board
     * @param numHumans Number of humans to create
     * @param numRats Number of rats to create
     * @param numPets Number of pets to create
     * @param initiallyInfected Number of entities to infect at the start
     * @param maxTurns Maximum number of turns for the simulation
     */
    public Simulation(int boardWidth, int boardHeight,
                      int numHumans, int numRats, int numPets,
                      int initiallyInfected, int maxTurns) {
        this.board = new Board(boardWidth, boardHeight);
        this.entityFactory = new EntityFactory(this.board);
        this.entityList = new ArrayList<>();
        this.currentTurn = 0;
        this.maxTurns = maxTurns;

        logger = new DataLogger();

        SwingUtilities.invokeLater(() -> {
            this.window = new SimulationWindow(this);
            initializeEntities(numHumans, numRats, numPets);
            setInitialInfected(initiallyInfected);
            window.updateDisplay();
            new Thread(this::runSimulation).start();
        });
    }

    /**
     * Runs the simulation in a separate thread.
     * Continuously executes turns until the simulation ends.
     */
    private void runSimulation() {
        while(running && shouldSimulationContinue()) {
            currentTurn++;
            executeTurn();

            SwingUtilities.invokeLater(() -> window.updateDisplay());

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }

        if (running) {
            SwingUtilities.invokeLater(() -> window.showFinalStatistics(generateFinalStats()));
        }
    }

    /**
     * Initializes entities on the board.
     * @param humanCount Number of humans to create
     * @param ratCount Number of rats to create
     * @param petCount Number of pets to create
     */
    private void initializeEntities(int humanCount, int ratCount, int petCount) {
        for (int i = 0; i < humanCount; i++) {
            entityList.add(entityFactory.createHuman());
        }
        for (int i = 0; i < ratCount; i++) {
            entityList.add(entityFactory.createRat());
        }
        for (int i = 0; i < petCount; i++) {
            entityList.add(entityFactory.createPet());
        }
    }

    /**
     * Sets initially infected entities.
     * @param count Number of entities to infect
     */
    private void setInitialInfected(int count) {
        if (count <= 0 || entityList.isEmpty())
            return;

        int infectedSet = 0;
        List<Entity> infectionCandidates = new ArrayList<>(entityList);
        java.util.Collections.shuffle(infectionCandidates);

        for (Entity entity : infectionCandidates) {
            if (infectedSet >= count)
                break;
            if (entity.getHealthStatus() == HealthStatus.HEALTHY) {
                entity.becomeInfected();
                infectedSet++;
            }
        }
    }

    /**
     * Starts the main simulation loop.
     */
    @SuppressWarnings("BusyWait")
    public void start() {
        while(shouldSimulationContinue()) {
            currentTurn++;
            executeTurn();
            generateStatsString();

            // Update UI in EDT
            SwingUtilities.invokeLater(() -> window.updateDisplay());

            try {
                Thread.sleep(500); // Pause between turns
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        logger.saveResults();
        SwingUtilities.invokeLater(() -> window.showFinalStatistics(generateFinalStats()));
    }

    /**
     * Executes all steps for a single simulation turn.
     */
    private void executeTurn() {
        // Entity movement
        for(Entity entity : entityList) entity.move(board);

        // Disease spread
        List<Entity> entityListCopy = new ArrayList<>(entityList);
        for(Entity infectingEntity : entityListCopy) infectingEntity.tryToInfectOthers(entityList, board);

        // Update of the state of entities
        for(Entity entity : entityList) entity.updateDiseaseState();
    }

    /**
     * Checks if the simulation should continue.
     * @return true if simulation should continue, false otherwise
     */
    private boolean shouldSimulationContinue() {
        if(currentTurn >= maxTurns) return false;

        long infectedEntitiesCount = entityList.stream()
                .filter(entity -> entity.getHealthStatus() == HealthStatus.INFECTED)
                .count();
        long aliveEntitiesCount = entityList.stream()
                .filter(entity -> entity.getHealthStatus() != HealthStatus.DECEASED)
                .count();

        if(infectedEntitiesCount == 0 && currentTurn > 0) {
            return false;
        }

        return aliveEntitiesCount != 0;
    }

    /**
     * Generates final statistics string.
     * @return Formatted statistics string
     */
    private String generateFinalStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("Final statistics after ").append(currentTurn).append(" turns:\n");
        sb.append(generateStatsString());

        sb.append("\nBreakdown by type:\n");
        entityList.stream()
                .collect(Collectors.groupingBy(Entity::getEntityType, Collectors.counting()))
                .forEach((type, count) -> sb.append("  ").append(type).append(": ").append(count).append("\n"));

        sb.append("\nBreakdown by type and health status:\n");
        entityList.stream()
                .collect(Collectors.groupingBy(Entity::getEntityType,
                        Collectors.groupingBy(Entity::getHealthStatus, Collectors.counting())))
                .forEach((type, statusStats) -> {
                    sb.append("  ").append(type).append(":\n");
                    statusStats.forEach((status, count) -> sb.append("    - ").append(status).append(": ").append(count).append("\n"));
                });

        return sb.toString();
    }

    /**
     * Generates current statistics string.
     * @return Formatted statistics string
     */
    private String generateStatsString() {
        long healthy = countByStatus(HealthStatus.HEALTHY);
        long infected = countByStatus(HealthStatus.INFECTED);
        long recovered = countByStatus(HealthStatus.RECOVERED);
        long deceased = countByStatus(HealthStatus.DECEASED);
        logger.saveTurnState(currentTurn, healthy, recovered, infected, deceased);
        return "  Healthy: " + healthy + "\n" +
                "  Infected: " + infected + "\n" +
                "  Recovered: " + recovered + "\n" +
                "  Deceased: " + deceased + "\n" +
                "  Total alive: " + (healthy + infected + recovered) + "\n";
    }

    /**
     * Counts entities by health status.
     * @param status Health status to count
     * @return Number of entities with given status
     */
    private long countByStatus(HealthStatus status) {
        return entityList.stream()
                .filter(entity -> entity.getHealthStatus() == status)
                .count();
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

    public Board getBoard() {
        return board;
    }
}