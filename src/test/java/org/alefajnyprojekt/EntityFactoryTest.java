package org.alefajnyprojekt;

import org.alefajnyprojekt.entities.Entity;
import org.alefajnyprojekt.entities.Human;
import org.alefajnyprojekt.entities.Pet;
import org.alefajnyprojekt.entities.Rat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityFactoryTest {

    private Board board;
    private EntityFactory factory;

    @BeforeEach
    void setUp() {
        board = new Board(10, 10);
        factory = new EntityFactory(board);
    }

    @Test
    void createHumanShouldReturnHumanInstance() {
        Entity human = factory.createHuman();
        assertInstanceOf(Human.class, human, "Factory should create an instance of Human.");
        assertNotNull(human.getPosition(), "Created human should have a position.");
        assertTrue(board.isInBounds(human.getPosition()), "Created human should be within board bounds.");
    }

    @Test
    void createHumanWithSpecificPositionShouldWork() {
        Position specificPos = new Position(3, 4);
        Human human = factory.createHuman(specificPos);
        assertEquals(specificPos, human.getPosition(), "Human should be created at the specified position.");
    }

    @Test
    void createRatShouldReturnRatInstance() {
        Entity rat = factory.createRat();
        assertInstanceOf(Rat.class, rat, "Factory should create an instance of Rat.");
        assertNotNull(rat.getPosition(), "Created rat should have a position.");
        assertTrue(board.isInBounds(rat.getPosition()), "Created rat should be within board bounds.");
    }

    @Test
    void createPetShouldReturnPetInstance() {
        Entity pet = factory.createPet();
        assertInstanceOf(Pet.class, pet, "Factory should create an instance of Pet.");
        assertNotNull(pet.getPosition(), "Created pet should have a position.");
        assertTrue(board.isInBounds(pet.getPosition()), "Created pet should be within board bounds.");
    }

    @Test
    void createRandomEntityShouldReturnNonNullEntity() {
        for (int i = 0; i < 10; i++) { // Test multiple random creations
            Entity randomEntity = factory.createRandomEntity();
            assertNotNull(randomEntity, "Randomly created entity should not be null.");
            assertTrue(randomEntity instanceof Human || randomEntity instanceof Rat || randomEntity instanceof Pet,
                    "Randomly created entity should be one of the known types.");
            assertNotNull(randomEntity.getPosition(), "Randomly created entity should have a position.");
            assertTrue(board.isInBounds(randomEntity.getPosition()), "Randomly created entity should be within board bounds.");
        }
    }
}