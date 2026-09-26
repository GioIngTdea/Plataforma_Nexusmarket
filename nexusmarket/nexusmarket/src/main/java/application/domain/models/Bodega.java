package application.domain.models;

import application.domain.valueobjects.WarehouseOwnerType;
import lombok.Getter;
import lombok.Setter;

/**
 * A physical storage space for inventory. Warehouses may belong to the
 * marketplace itself or be managed by a specific seller.
 */
@Getter
@Setter
public class Bodega {
    private String id;
    private String location;
    private int capacity;
    private WarehouseOwnerType ownerType;
    /** The owning seller, present only when ownerType is SELLER. */
    private Vendedor ownerSeller;
    private Administrador registeredBy;
}
