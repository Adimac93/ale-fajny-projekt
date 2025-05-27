package org.alefajnyprojekt.entities;

import org.alefajnyprojekt.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Class representing a human in the simulation.
 * Inherits from Entity and may have specific behaviors or attributes.
 */
public class Human extends Entity {

    // Specific attributes for humans could be added, e.g., age.
    // For simplicity, attributes from the Entity class are used

    /**
     * Constructor for a Human object.
     * @param position Initial position of the human.
     * @param immunity Immunity level (0-1). Default immunity for humans might be e.g., between 0.1 and 0.4.
     */
    public Human(Position position, double immunity) {
        super(position,
              immunity,  // Specific immunity for humans
              1,         // Human movement range (in fields)
              2,         // Human infection range (in fields)
              0.6);      // Chance for a human to infect others
    }

    /**
     * Constructor for a Human object with default immunity.
     * @param position Initial position of the human.
     */
    public Human(Position position) {
        this(position, ThreadLocalRandom.current().nextDouble(0.1, 0.4)); // Random immunity in range
    }

    @Override
    protected int determineTurnsForStateChangeAfterInfection() {
        // Humans are sick for e.g., 7 to 14 days (turns)
        return ThreadLocalRandom.current().nextInt(14, 31);
    }

    @Override
    public String getEntityType() {
        return "Human";
    }

}