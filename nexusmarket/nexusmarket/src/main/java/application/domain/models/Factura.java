package application.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * The commercial record associated with the sale of an order.
 */
@Getter
@Setter
public class Factura {
    private String id;
    private Pedido order;
    private BigDecimal totalAmount;
}
