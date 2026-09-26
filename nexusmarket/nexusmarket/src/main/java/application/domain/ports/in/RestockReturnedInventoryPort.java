package application.domain.ports.in;

public interface RestockReturnedInventoryPort {
    void execute(String productId, String warehouseId, int quantity);
}
