package tn.esprit.Users.entities;

public enum UserStatus {
    ACTIVE,           // Currently working
    INACTIVE,         // Not currently working but still employed
    ON_LEAVE,         // On temporary leave (e.g., vacation, sick leave)
    SUSPENDED,        // Temporarily suspended due to disciplinary reasons
    TERMINATED,       // Employment ended by the company
    RESIGNED,         // Left the company voluntarily
    RETIRED,          // No longer working due to retirement
    PROBATION,        // New employee under evaluation
    CONTRACT_ENDED    // Temporary contract has expired
}
