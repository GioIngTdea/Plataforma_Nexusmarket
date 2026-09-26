package application.domain.models;

import application.domain.valueobjects.RefundStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * The refund of money associated with an approved return.
 * <p>
 * The functional specification does not detail this entity's attributes or
 * its structural relationship to Pedido/Devolucion; the fields below are a
 * documented design assumption (see gestion-postventa.md) meant to be
 * validated with the business.
 */
@Getter
@Setter
public class Reembolso {
    private String id;
    private Devolucion devolucion;
    private BigDecimal amount;
    private RefundStatus status;
    private Administrador processedBy;
}
