package org.alefajnyprojekt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void constructorShouldSetDimensions() {
        Board board = new Board(10, 20);
        assertEquals(10, board.getWidth(), "Board width should be set correctly.");
        assertEquals(20, board.getHeight(), "Board height should be set correctly.");
    }

    @Test
    void constructorShouldThrowExceptionForNonPositiveWidth() {
        assertThrows(IllegalArgumentException.class, () -> new Board(0, 20),
                "Constructor should throw IllegalArgumentException for zero width.");
        assertThrows(IllegalArgumentException.class, () -> new Board(-1, 20),
                "Constructor should throw IllegalArgumentException for negative width.");
    }

    @Test
    void constructorShouldThrowExceptionForNonPositiveHeight() {
        assertThrows(IllegalArgumentException.class, () -> new Board(10, 0),
                "Constructor should throw IllegalArgumentException for zero height.");
        assertThrows(IllegalArgumentException.class, () -> new Board(10, -5),
                "Constructor should throw IllegalArgumentException for negative height.");
    }

    @Test
    void isInBoundsShouldReturnTrueForValidCoordinates() {
        Board board = new Board(10, 10);
        assertTrue(board.isInBounds(0, 0), "Position (0,0) should be in bounds.");
        assertTrue(board.isInBounds(9, 9), "Position (9,9) should be in bounds for a 10x10 board.");
        assertTrue(board.isInBounds(5, 5), "Position (5,5) should be in bounds.");
    }

    @Test
    void isInBoundsShouldReturnFalseForInvalidCoordinates() {
        Board board = new Board(10, 10);
        assertFalse(board.isInBounds(-1, 5), "Negative X coordinate should be out of bounds.");
        assertFalse(board.isInBounds(5, -1), "Negative Y coordinate should be out of bounds.");
        assertFalse(board.isInBounds(10, 5), "X coordinate equal to width should be out of bounds.");
        assertFalse(board.isInBounds(5, 10), "Y coordinate equal to height should be out of bounds.");
        assertFalse(board.isInBounds(15, 15), "Coordinates larger than dimensions should be out of bounds.");
    }

    @Test
    void isInBoundsWithPositionObjectShouldWork() {
        Board board = new Board(10, 10);
        assertTrue(board.isInBounds(new Position(0, 0)), "Position object (0,0) should be in bounds.");
        assertFalse(board.isInBounds(new Position(10, 5)), "Position object (10,5) should be out of bounds.");
    }

    @Test
    void generateRandomPositionShouldBeWithinBounds() {
        Board board = new Board(5, 5); // Mała plansza dla łatwiejszego testowania
        for (int i = 0; i < 100; i++) { // Wygeneruj kilka losowych pozycji
            Position randomPos = board.generateRandomPosition();
            assertTrue(randomPos.getX() >= 0 && randomPos.getX() < board.getWidth(),
                    "Generated random X coordinate should be within bounds.");
            assertTrue(randomPos.getY() >= 0 && randomPos.getY() < board.getHeight(),
                    "Generated random Y coordinate should be within bounds.");
            assertTrue(board.isInBounds(randomPos),
                    "Generated random Position object should be within bounds.");
        }
    }
}