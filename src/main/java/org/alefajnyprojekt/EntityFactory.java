package org.alefajnyprojekt;

import org.alefajnyprojekt.entities.Entity;
import org.alefajnyprojekt.entities.Human;
import org.alefajnyprojekt.entities.Pet;
import org.alefajnyprojekt.entities.Rat;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Factory for creating different types of entities.
 * Facilitates object creation management and maintains consistency.
 */
public class EntityFactory {

    private final Board board;

    public EntityFactory(Board board) {
        this.board = board;
    }

    public Human createHuman() {
        return new Human(board.generateRandomPosition());
    }
    public Human createHuman(Position position) {
        return new Human(position);
    }
    public Human createHuman(Position position, double immunity) {
        return new Human(position, immunity);
    }

    public Rat createRat() {
        return new Rat(board.generateRandomPosition());
    }
    public Rat createRat(Position position) {
        return new Rat(position);
    }

    public Pet createPet() {
        return new Pet(board.generateRandomPosition());
    }
    public Pet createPet(Position position) {
        return new Pet(position);
    }

    /**
     * Creates a random entity at a random position.
     * @return A randomly selected entity type.
     */
    public Entity createRandomEntity() {
        int type = ThreadLocalRandom.current().nextInt(3); // 0: Human, 1: Rat, 2: Pet
        switch (type) {
            case 0: return createHuman();
            case 1: return createRat();
            case 2: default: return createPet();
        }
    }
}