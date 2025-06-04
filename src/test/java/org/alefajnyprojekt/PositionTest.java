package org.alefajnyprojekt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void constructorAndGettersShouldWork() {
        Position pos = new Position(10, 20);
        assertEquals(10, pos.getX(), "X coordinate should be set correctly.");
        assertEquals(20, pos.getY(), "Y coordinate should be set correctly.");
    }

    @Test
    void settersShouldUpdateCoordinates() {
        Position pos = new Position(5, 5);
        pos.setX(15);
        pos.setY(25);
        assertEquals(15, pos.getX(), "X coordinate should be updated by setter.");
        assertEquals(25, pos.getY(), "Y coordinate should be updated by setter.");
    }

    @Test
    void distanceToShouldCalculateCorrectly() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(3, 4);
        assertEquals(5.0, pos1.distanceTo(pos2), 0.001, "Distance calculation should be correct.");
    }

    @Test
    void distanceToSamePositionShouldBeZero() {
        Position pos1 = new Position(10, 10);
        Position pos2 = new Position(10, 10);
        assertEquals(0.0, pos1.distanceTo(pos2), 0.001, "Distance to the same position should be zero.");
    }

    @Test
    void equalsShouldReturnTrueForSameCoordinates() {
        Position pos1 = new Position(7, 8);
        Position pos2 = new Position(7, 8);
        assertEquals(pos1, pos2, "Equals should return true for positions with the same coordinates.");
        assertEquals(pos1.hashCode(), pos2.hashCode(), "Hash codes should be equal for equal objects.");
    }

    @Test
    void equalsShouldReturnFalseForDifferentCoordinates() {
        Position pos1 = new Position(7, 8);
        Position pos2 = new Position(8, 7);
        assertNotEquals(pos1, pos2, "Equals should return false for positions with different coordinates.");
    }

    @Test
    void equalsShouldReturnFalseForNull() {
        Position pos1 = new Position(1, 1);
        assertNotEquals(null, pos1, "Equals should return false when compared with null.");
    }

    @Test
    void equalsShouldReturnFalseForDifferentClass() {
        Position pos1 = new Position(1, 1);
        Object other = new Object();
        assertNotEquals(pos1, other, "Equals should return false when compared with an object of a different class.");
    }

    @Test
    void toStringShouldReturnCorrectFormat() {
        Position pos = new Position(3, 5);
        assertEquals("(3, 5)", pos.toString(), "toString should return coordinates in (x, y) format.");
    }
}