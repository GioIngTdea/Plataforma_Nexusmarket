package application.domain.valueobjects;

/**
 * Lifecycle status of a {@code Pedido}, corresponding to the domain's
 * EstadoPedido enumeration.
 * Valid forward path: PENDING_PAYMENT -> PAID -> DISPATCHED -> DELIVERED_COMPLETED.
 * PENDING_PAYMENT may also transition to CANCELLED.
 * DELIVERED_COMPLETED is final and admits no further modification.
 */
public enum OrderStatus {
    PENDING_PAYMENT,
    PAID,
    DISPATCHED,
    DELIVERED_COMPLETED,
    CANCELLED
}
