import java.util.List;
import java.util.Random;

/**
 * Abstract class representing an entity in the simulation (e.g., human, animal).
 * Each entity has a position, health status, immunity, movement range, and infection range.
 */
public abstract class Entity {
    private static int idCounter = 0; // Static counter for generating unique IDs
    protected final int id;
    protected Position position;
    protected HealthStatus healthStatus;
    protected double immunity; // e.g., 0.0 (none) to 1.0 (full immunity to infection)
    protected int movementRange;
    protected int infectionRange;
    protected double chanceToInfectOthers; // Probability that this entity infects another within range (0.0 - 1.0)
    protected int turnsUntilStateChange; // How many turns are left for recovery/death after infection

    protected static final Random random = new Random();

    /**
     * Constructor for an abstract Entity.
     * @param position Initial position of the entity.
     * @param immunity Immunity level of the entity (0-1).
     * @param movementRange Maximum distance the entity can travel in one turn.
     * @param infectionRange Range at which the entity can spread the disease.
     * @param chanceToInfectOthers Probability of infecting another entity.
     */
    public Entity(Position position, double immunity, int movementRange, int infectionRange, double chanceToInfectOthers) {
        this.id = ++idCounter;
        this.position = position;
        this.healthStatus = HealthStatus.HEALTHY; // By default, every new entity is healthy
        this.immunity = Math.max(0.0, Math.min(1.0, immunity)); // Ensure immunity is within [0,1]
        this.movementRange = movementRange;
        this.infectionRange = infectionRange;
        this.chanceToInfectOthers = Math.max(0.0, Math.min(1.0, chanceToInfectOthers));
        this.turnsUntilStateChange = 0;
    }

    // Getters
    public int getId() { return id; }
    public Position getPosition() { return position; }
    public HealthStatus getHealthStatus() { return healthStatus; }
    public double getImmunity() { return immunity; }
    public int getMovementRange() { return movementRange; }
    public int getInfectionRange() { return infectionRange; }
    public double getChanceToInfectOthers() { return chanceToInfectOthers; }
    public int getTurnsUntilStateChange() { return turnsUntilStateChange; }


    // Setters (some might be unnecessary or require logic)
    public void setPosition(Position position) { this.position = position; }

    /**
     * Tries to infect the entity. Succeeds if a random value is greater than the entity's immunity.
     * @return true if the entity was infected, false otherwise.
     */
    public boolean tryToGetInfected() {
        if (this.healthStatus == HealthStatus.HEALTHY) {
            if (random.nextDouble() > this.immunity) {
                becomeInfected();
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the entity's state to INFECTED and sets the turn counter for recovery/death.
     * This method should be overridden in derived classes to set a specific time.
     */
    protected void becomeInfected() {
        this.healthStatus = HealthStatus.INFECTED;
        // Setting time until state change should be more specific to the entity type
        // e.g., Human 7-14 days, Rat 3-5 days
        this.turnsUntilStateChange = determineTurnsForStateChangeAfterInfection();
        System.out.println(this.getEntityType() + " " + id + " became infected. Turns until state change: " + this.turnsUntilStateChange);
    }

    /**
     * Abstract method determining the number of turns until state change after infection.
     * Must be implemented by inheriting classes.
     * @return Number of turns.
     */
    protected abstract int determineTurnsForStateChangeAfterInfection();

    /**
     * Abstract method returning the entity type as a String.
     * @return Entity type.
     */
    public abstract String getEntityType();


    /**
     * Logic for the entity's movement on the board.
     * The entity moves in a random direction for a random distance within its movement range.
     * @param board The board on which the entity moves.
     */
    public void move(Board board) {
        if (this.healthStatus == HealthStatus.DECEASED) return; // Deceased entities don't move

        int distance = random.nextInt(this.movementRange + 1); // Random distance from 0 to movementRange
        if (distance == 0) return; // May not move

        for (int i = 0; i < distance; i++) { // Attempt to move 1 unit 'distance' times
            Direction direction = Direction.getRandomDirection();
            if (direction == Direction.STATIONARY && distance > 0) continue; // If it has to move, pick another direction

            int newX = this.position.getX() + direction.getDeltaX();
            int newY = this.position.getY() + direction.getDeltaY();

            // Check if the new position is within board boundaries
            if (board.isInBounds(newX, newY)) {
                this.position.setX(newX);
                this.position.setY(newY);
            } else {
                // Optionally: bounce off the wall or stop
                break; // Stops at the edge
            }
        }
         // System.out.println(getEntityType() + " " + id + " moved to: " + position);
    }

    /**
     * Logic for spreading the disease.
     * If the entity is infected, it attempts to infect other entities within its infection range.
     * @param allEntities List of all entities in the simulation.
     * @param board The simulation board (for boundary checks, if needed).
     */
    public void tryToInfectOthers(List<Entity> allEntities, Board board) {
        if (this.healthStatus != HealthStatus.INFECTED) {
            return;
        }

        for (Entity otherEntity : allEntities) {
            if (otherEntity == this || otherEntity.getHealthStatus() == HealthStatus.DECEASED || otherEntity.getHealthStatus() == HealthStatus.INFECTED) {
                continue; // Do not infect self, deceased, or already infected entities
            }

            // Check if the other entity is within infection range
            if (this.position.distanceTo(otherEntity.getPosition()) <= this.infectionRange) {
                // Check if infection is successful (based on this entity's chance to infect)
                if (random.nextDouble() < this.chanceToInfectOthers) {
                    // The other entity attempts to get infected (considering its own immunity)
                    if (otherEntity.tryToGetInfected()) {
                         System.out.println(this.getEntityType() + " " + id + " (at " + this.position +") infected " + otherEntity.getEntityType() + " " + otherEntity.getId() + " (at " + otherEntity.getPosition() + ")");
                    }
                }
            }
        }
    }

    /**
     * Updates the entity's disease state based on the passage of time (turns).
     * If the entity is infected, it decrements the turnsUntilStateChange counter.
     * After this time, the entity may recover or die.
     */
    public void updateDiseaseState() {
        if (this.healthStatus == HealthStatus.INFECTED) {
            this.turnsUntilStateChange--;
            if (this.turnsUntilStateChange <= 0) {
                // Decision to recover or die (can depend on entity type, immunity, etc.)
                // For simplicity, let's assume an 80% chance of recovery
                if (random.nextDouble() < 0.80) { // 80% chance of recovery
                    this.healthStatus = HealthStatus.RECOVERED;
                    // Optionally: increase immunity after recovery
                    this.immunity = Math.min(1.0, this.immunity + 0.5); // E.g., increase immunity
                    System.out.println(this.getEntityType() + " " + id + " has recovered.");
                } else {
                    this.healthStatus = HealthStatus.DECEASED;
                    System.out.println(this.getEntityType() + " " + id + " has died from the disease.");
                }
            }
        }
    }

    @Override
    public String toString() {
        return getEntityType() + " ID=" + id +
               ", Position=" + position +
               ", Status=" + healthStatus +
               ", Immunity=" + String.format("%.2f", immunity) +
               ", MoveRange=" + movementRange +
               ", InfectionRange=" + infectionRange +
               (healthStatus == HealthStatus.INFECTED ? ", TurnsToChange=" + turnsUntilStateChange : "");
    }
}