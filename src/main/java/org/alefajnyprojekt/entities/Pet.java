package org.alefajnyprojekt.entities;

import org.alefajnyprojekt.Position;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Class representing a pet in the simulation (e.g., dog, cat).
 * They move similarly to humans but can be more effective at infecting.
 */
public class Pet extends Animal {

    /**
     * Constructor for a Pet object.
     *
     * @param position Initial position of the pet.
     */
    public Pet(Position position) {
        super(position,
              ThreadLocalRandom.current().nextDouble(0.1, 0.3), // Immunity similar to humans or slightly lower
              1,         // Moves at human pace (smaller movement range than rat)
              1,         // Infection range can be similar to a human
              0.7);      // More effective at infecting than humans (higher chance)
    }

     @Override
    protected int determineTurnsForStateChangeAfterInfection() {
        // Pets are sick for a duration similar to humans, e.g., 5-10 days (turns)
        return ThreadLocalRandom.current().nextInt(7, 24);
    }

    @Override
    public String getEntityType() {
        return "Pet";
    }
}