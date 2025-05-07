import java.util.concurrent.ThreadLocalRandom;

/**
 * Class representing a rat in the simulation.
 * Rats move quickly but have a small infection range.
 */
public class Rat extends Animal {

    /**
     * Constructor for a Rat object.
     * @param position Initial position of the rat.
     */
    public Rat(Position position) {
        super(position,
              ThreadLocalRandom.current().nextDouble(0.05, 0.2), // Low immunity for rats
              3,         // Rats are fast, larger movement range
              1,         // Small infection range
              0.4);      // Moderate chance to infect others
    }

    @Override
    protected int determineTurnsForStateChangeAfterInfection() {
        // Rats are sick for a short time, e.g., 3-5 days (turns)
        return ThreadLocalRandom.current().nextInt(3, 6);
    }

    @Override
    public String getEntityType() {
        return "Bulbazaur";
    }

    // Methods can be overridden if rats have specific behaviors,
    // e.g., preferring certain areas of the board.
}