/**
 * Abstract class representing an animal in the simulation.
 * Inherits from Entity. It is a base for specific animal types.
 */
public abstract class Animal extends Entity {

    /**
     * Constructor for an abstract Animal.
     * @param position Initial position of the animal.
     * @param immunity Animal's immunity level (0-1).
     * @param movementRange Animal's maximum movement distance.
     * @param infectionRange Animal's infection range.
     * @param chanceToInfectOthers Chance that the animal infects others.
     */
    public Animal(Position position, double immunity, int movementRange, int infectionRange, double chanceToInfectOthers) {
        super(position, immunity, movementRange, infectionRange, chanceToInfectOthers);
    }
    // Inheriting classes (Rat, Pet) must implement
    // getEntityType() and determineTurnsForStateChangeAfterInfection().
}