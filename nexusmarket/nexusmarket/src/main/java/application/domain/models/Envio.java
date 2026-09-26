package application.domain.models;

import lombok.Getter;
import lombok.Setter;

/**
 * The packing, dispatch and transport process associated with an order
 * that contains physical products.
 */
@Getter
@Setter
public class Envio {
    private String id;
    private Pedido order;
    private OperadorLogistico handledBy;
}
