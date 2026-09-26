package application.domain.models;

import application.domain.valueobjects.ReturnStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * A buyer's request to return a product acquired in a completed order.
 * <p>
 * The functional specification does not detail this entity's attributes or
 * its formal relationship to Pedido/Reembolso; the fields below are a
 * documented design assumption (see gestion-postventa.md) meant to be
 * validated with the business.
 */
@Getter
@Setter
public class Devolucion {
    private String id;
    private Pedido order;
    private Producto product;
    private Comprador requestedBy;
    private String reason;
    private ReturnStatus status;
    /** The administrator or seller who resolved the request, once resolved. */
    private Usuario resolvedBy;
}
