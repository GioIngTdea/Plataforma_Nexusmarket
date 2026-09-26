package application.domain.valueobjects;

/**
 * Reason recorded for a change in a product's available stock at a warehouse.
 */
public enum InventoryMovementType {
    ENTRY,
    RESERVATION,
    SALE_EXIT,
    ADJUSTMENT,
    RETURN
}
