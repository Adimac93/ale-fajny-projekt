package org.alefajnyprojekt.enums;

/**
 * Enum representing the health status of an entity.
 * Entities can be healthy, infected, recovered, or dead.
 */
public enum HealthStatus {
    HEALTHY,    // Entity is healthy and susceptible to infection
    INFECTED,   // Entity is infected and can infect others
    RECOVERED,  // Entity has recovered and (potentially) gained immunity
    DECEASED    // Entity has died
}