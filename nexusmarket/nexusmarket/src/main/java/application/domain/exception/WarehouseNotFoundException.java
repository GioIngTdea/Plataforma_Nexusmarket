package application.domain.exception;

public class WarehouseNotFoundException extends RuntimeException {
    public WarehouseNotFoundException(String warehouseId) {
        super("No warehouse found with id: " + warehouseId);
    }
}
