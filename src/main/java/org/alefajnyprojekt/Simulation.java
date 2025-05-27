package org.alefajnyprojekt;

import org.alefajnyprojekt.entities.Entity;
import org.alefajnyprojekt.enums.HealthStatus;

import java.util.ArrayList;
import java.util.List;


/**
 * Main class managing the disease spread simulation.
 * Responsible for initializing the board, entities, turn progression, and
 * simulation logic.
 */
public class Simulation {
    private final Board board;
    private List<Entity> entityList;
    private final EntityFactory entityFactory;
    private int currentTurn;
    private final int maxTurns;

    /**
     * Simulation constructor.
     *
     * @param boardWidth            Width of the board.
     * @param boardHeight           Height of the board.
     * @param numHumans             Number of humans at the start.
     * @param numRats               Number of rats at the start.
     * @param numPets               Number of pets at the start.
     * @param initiallyInfected     Number of entities that start as infected.
     * @param maxTurns              Maximum number of simulation turns.
     */
     public Simulation(int boardWidth, int boardHeight,
                       int numHumans, int numRats, int numPets,
                       int initiallyInfected, int maxTurns) {
         this.board = new Board(boardWidth, boardHeight);
         this.entityFactory = new EntityFactory(this.board);
         this.entityList = new ArrayList<>();
         this.currentTurn = 0;
         this.maxTurns = maxTurns;

         initializeEntities(numHumans, numRats, numPets);
         setInitialInfected(initiallyInfected);
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
        if (count <= 0 || entityList.isEmpty())
            return;

        int infectedSet = 0;
        List<Entity> infectionCandidates = new ArrayList<>(entityList);
        java.util.Collections.shuffle(infectionCandidates); // Shuffle the list to pick randomly

        for (Entity entity : infectionCandidates) {
            if (infectedSet >= count)
                break;
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

        while(shouldSimulationContinue()) {
            currentTurn++;
            System.out.println("\n--- Turn: " + currentTurn + " ---");
            executeTurn();
            displaySimulationState();
        }
        displayFinalStatistics();
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
     * (Checks if there are still infected entities or if the turn limit has not been
     * reached).
     *
     * @return true if the simulation should continue, false otherwise.
     */
    private boolean shouldSimulationContinue() {
        long infectedEntitiesCount = entityList.stream()
                .filter(entity -> entity.getHealthStatus() == HealthStatus.INFECTED)
                .count();
        long aliveEntitiesCount = entityList.stream()
                .filter(entity -> entity.getHealthStatus() != HealthStatus.DECEASED)
                .count();

        // During the turn 0 infection state of entities is not yet changed
        if(infectedEntitiesCount == 0 && currentTurn > 0) {
            System.out.println("No infected entities left. Epidemic ends.");
            return false;
        }

        if(aliveEntitiesCount == 0) {
            System.out.println("No alive entities left. Epidemic ends.");
            return false;
        }
        return true;
    }
    void displaySimulationState() {}
    void displayFinalStatistics() {}
}