package application.domain.models;

import lombok.Getter;
import lombok.Setter;

/**
 * A user who sells products in the marketplace. Sellers cannot self-register:
 * they are onboarded exclusively by an Administrador, together with their
 * first warehouse.
 */
@Getter
@Setter
public class Vendedor extends Usuario {
    private Administrador registeredBy;
}
