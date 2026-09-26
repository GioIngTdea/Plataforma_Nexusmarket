package application.domain.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(String productId, String warehouseId) {
        super("No inventory record found for product " + productId + " at warehouse " + warehouseId);
    }
}
