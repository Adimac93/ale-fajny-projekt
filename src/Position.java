/**
 * Class representing a position (X, Y coordinates) on the board.
 * Used by entities to determine their location.
 */
public class Position {
    private int x;
    private int y;

    /**
     * Constructor for the Position object.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Setters
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Calculates the Euclidean distance to another position.
     * @param otherPosition The other position.
     * @return The distance between the positions.
     */
    public double distanceTo(Position otherPosition) {
        int dx = this.x - otherPosition.getX();
        int dy = this.y - otherPosition.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }
}