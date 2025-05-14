package org.alefajnyprojekt;

import org.alefajnyprojekt.entities.Entity;

/**
 * Class representing the two-dimensional simulation board.
 * Stores information about its dimensions. It could also store references to entities
 * on it, but for simplicity, entities currently know their own position.
 * The board mainly defines boundaries.
 */
public class Board {
    private final int width;
    private final int height;

    /**
     * Constructor for the Board object.
     * @param width The width of the board.
     * @param height The height of the board.
     */
    public Board(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Board dimensions must be positive.");
        }
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    /**
     * Checks if a given position (x, y) is within the board boundaries.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @return true if the position is within board boundaries, false otherwise.
     */
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Checks if a given Position is within the board boundaries.
     * @param position The Position object to check.
     * @return true if the position is within board boundaries, false otherwise.
     */
    public boolean isInBounds(Position position) {
        return isInBounds(position.getX(), position.getY());
    }

    /**
     * Generates a random position on the board.
     * @return A random Position object.
     */
    public Position generateRandomPosition() {
        int x = Entity.random.nextInt(width);
        int y = Entity.random.nextInt(height);
        return new Position(x, y);
    }
}