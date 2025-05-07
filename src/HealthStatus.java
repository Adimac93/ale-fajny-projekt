/**
 * Enum representing the health status of an entity.
 * Entities can be healthy, infected, recover, or die.
 */
public enum HealthStatus {
    HEALTHY,    // Entity is healthy and susceptible to infection
    INFECTED,   // Entity is infected and can infect others
    RECOVERED,  // Entity has recovered and (potentially) gained immunity
    DECEASED    // Entity has died
}