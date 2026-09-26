package application.domain.valueobjects;

/**
 * Operational status of a user account.
 * Allowed transitions: ACTIVE <-> SUSPENDED, ACTIVE -> BLOCKED, SUSPENDED -> BLOCKED.
 * BLOCKED is permanent and has no outgoing transition.
 */
public enum UserStatus {
    ACTIVE,
    SUSPENDED,
    BLOCKED
}
