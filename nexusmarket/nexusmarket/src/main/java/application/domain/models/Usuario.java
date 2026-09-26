package application.domain.models;

import application.domain.valueobjects.Role;
import application.domain.valueobjects.UserStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents any person authorized to interact with NexusMarket.
 * Centralizes identification and account data shared by every business
 * role (Comprador, Vendedor, OperadorLogistico, Administrador, Supervisor).
 * This class cannot be instantiated directly.
 */
@Getter
@Setter
public abstract class Usuario {
    private String id;
    private String fullName;
    private String email;
    private Role role;
    private UserStatus status;

    public boolean isActive() {
        return UserStatus.ACTIVE.equals(status);
    }
}
