public class Main {
    public static void main(String[] args) {
        System.out.println("Initializing disease spread simulator...");

        // Simulation parameters
        int boardWidth = 20;
        int boardHeight = 20;
        int numHumans = 50;
        int numRats = 10;
        int numPets = 5;
        int initiallyInfected = 3; // How many entities are infected at the start
        int maxTurns = 100;

        // Create and run the simulation
        Simulation simulation = new Simulation(
                boardWidth, boardHeight,
                numHumans, numRats, numPets,
                initiallyInfected, maxTurns
        );

        simulation.start();

        System.out.println("Program execution finished.");
    }
}