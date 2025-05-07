import java.util.Random;

/**
 * Enum representing possible movement directions on the board.
 * Each direction has associated changes for X and Y coordinates.
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP_LEFT(-1, -1),
    UP_RIGHT(1, -1),
    DOWN_LEFT(-1, 1),
    DOWN_RIGHT(1, 1),
    STATIONARY(0, 0); // Entity can also stay in place

    private final int deltaX;
    private final int deltaY;
    private static final Random random = new Random();
    private static final Direction[] DIRECTIONS = values();

    Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    /**
     * Returns a random movement direction (including STATIONARY).
     * @return A random Direction.
     */
    public static Direction getRandomDirection() {
        return DIRECTIONS[random.nextInt(DIRECTIONS.length)];
    }
}