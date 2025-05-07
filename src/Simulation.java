import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main class managing the disease spread simulation.
 * Responsible for initializing the board, entities, turn progression, and simulation logic.
 */
public class Simulation {
    private Board board;
    private List<Entity> entityList;
    private EntityFactory entityFactory;
    private int currentTurn;
    private int maxTurns;

    /**
     * Simulation constructor.
     * @param boardWidth Width of the board.
     * @param boardHeight Height of the board.
     * @param initialHumanCount Number of humans at the start.
     * @param initialRatCount Number of rats at the start.
     * @param initialPetCount Number of pets at the start.
     * @param initialInfectedCount Number of entities that start as infected.
     * @param maxTurns Maximum number of simulation turns.
     */
    public Simulation(int boardWidth, int boardHeight,
                     int initialHumanCount, int initialRatCount, int initialPetCount,
                     int initialInfectedCount, int maxTurns) {

        this.board = new Board(boardWidth, boardHeight);
        this.entityFactory = new EntityFactory(this.board);
        this.entityList = new ArrayList<>();
        this.currentTurn = 0;
        this.maxTurns = maxTurns;

        initializeEntities(initialHumanCount, initialRatCount, initialPetCount);
        setInitialInfected(initialInfectedCount);
    }

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
        System.out.println("Initialized " + entityList.size() + " entities.");
    }

    private void setInitialInfected(int count) {
        if (count <= 0 || entityList.isEmpty()) return;

        int infectedSet = 0;
        List<Entity> infectionCandidates = new ArrayList<>(entityList);
        java.util.Collections.shuffle(infectionCandidates); // Shuffle the list to pick randomly

        for (Entity entity : infectionCandidates) {
            if (infectedSet >= count) break;
            if (entity.getHealthStatus() == HealthStatus.HEALTHY) {
                entity.becomeInfected(); // becomeInfected method sets state to INFECTED and turnsUntilStateChange
                infectedSet++;
            }
        }
        System.out.println("Set " + infectedSet + " initially infected entities.");
    }

    /**
     * Starts the main simulation loop.
     */
    public void start() {
        System.out.println("Starting simulation...");
        displaySimulationState();

        while (currentTurn < maxTurns && shouldSimulationContinue()) {
            currentTurn++;
            System.out.println("\n--- Turn: " + currentTurn + " ---");
            executeTurn();
            displaySimulationState();

            try {
                Thread.sleep(200); // Pause for better observation (reduced from 500ms)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Simulation interrupted.");
                break;
            }
        }
        System.out.println("\n--- End of Simulation (turn: " + currentTurn + ") ---");
        displayFinalStatistics();
    }

    /**
     * Executes all steps for a single simulation turn.
     */
    private void executeTurn() {
        // 1. Entity movement
        for (Entity entity : entityList) {
            entity.move(board);
        }

        // 2. Disease spread
        // Copy the list to avoid ConcurrentModificationException if, e.g., an entity dies
        // and is removed from the main list during iteration (though not removing here in this loop).
        // More importantly, to ensure an entity's state at the beginning of the turn is considered for infection.
        List<Entity> entityListCopy = new ArrayList<>(entityList);
        for (Entity infectingEntity : entityListCopy) {
            if (infectingEntity.getHealthStatus() == HealthStatus.INFECTED) {
                // Pass the original list to modify states
                infectingEntity.tryToInfectOthers(entityList, board);
            }
        }

        // 3. Update disease state (recovery, death)
        for (Entity entity : entityList) {
            entity.updateDiseaseState();
        }

        // 4. Optionally: remove deceased entities (might be needed for performance)
        // entityList.removeIf(e -> e.getHealthStatus() == HealthStatus.DECEASED);
        // For now, keeping deceased entities for statistics.
    }

    /**
     * Checks if the simulation should continue.
     * (E.g., if there are still infected entities or if the turn limit has not been reached).
     * @return true if the simulation should continue, false otherwise.
     */
    private boolean shouldSimulationContinue() {
        long infectedEntitiesCount = entityList.stream()
                                          .filter(e -> e.getHealthStatus() == HealthStatus.INFECTED)
                                          .count();
        long livingEntitiesCount = entityList.stream()
                                          .filter(e -> e.getHealthStatus() != HealthStatus.DECEASED)
                                          .count();

        if (infectedEntitiesCount == 0 && currentTurn > 0) { // If infected at start, turn 0 is okay
            System.out.println("No infected entities left. Epidemic ends.");
            return false;
        }
        if (livingEntitiesCount == 0) {
            System.out.println("All entities have deceased.");
            return false;
        }
        return true;
    }

    /**
     * Displays the current state of the simulation (e.g., number of entities in each health state).
     */
    public void displaySimulationState() {
        System.out.println("State at the end of turn " + currentTurn + ":");
        long healthyCount = entityList.stream().filter(e -> e.getHealthStatus() == HealthStatus.HEALTHY).count();
        long infectedCount = entityList.stream().filter(e -> e.getHealthStatus() == HealthStatus.INFECTED).count();
        long recoveredCount = entityList.stream().filter(e -> e.getHealthStatus() == HealthStatus.RECOVERED).count();
        long deceasedCount = entityList.stream().filter(e -> e.getHealthStatus() == HealthStatus.DECEASED).count();

        System.out.println("  Healthy: " + healthyCount);
        System.out.println("  Infected: " + infectedCount);
        System.out.println("  Recovered: " + recoveredCount);
        System.out.println("  Deceased: " + deceasedCount);
        System.out.println("  Total alive: " + (healthyCount + infectedCount + recoveredCount));

        // Optionally: more detailed information or board visualization
        // for (Entity e : entityList) {
        //     System.out.println("    " + e.toString());
        // }
    }

    public void displayFinalStatistics() {
        System.out.println("Final statistics after " + currentTurn + " turns:");
        displaySimulationState(); // Display the final state again
        // More detailed statistics can be added, e.g., breakdown by entity type
        System.out.println("Breakdown by type:");
        entityList.stream()
            .collect(Collectors.groupingBy(Entity::getEntityType, Collectors.counting()))
            .forEach((type, count) -> System.out.println("  " + type + ": " + count + " (initially)"));

        System.out.println("\nBreakdown by type and health status:");
         entityList.stream()
            .collect(Collectors.groupingBy(Entity::getEntityType,
                Collectors.groupingBy(Entity::getHealthStatus, Collectors.counting())))
            .forEach((type, statusStats) -> {
                System.out.println("  " + type + ":");
                statusStats.forEach((status, count) ->
                    System.out.println("    - " + status + ": " + count)
                );
            });
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

    public Board getBoard() {
        return board;
    }
}