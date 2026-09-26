package application.domain.models;

import lombok.Getter;
import lombok.Setter;

/**
 * A user who purchases products in the marketplace: selects products,
 * manages a shopping cart, and confirms orders.
 */
@Getter
@Setter
public class Comprador extends Usuario {
    private String primaryAddress;
    private String additionalAddresses;
    private String commercialStatus;
}
