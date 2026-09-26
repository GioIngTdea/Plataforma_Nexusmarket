package application.domain.ports.in;

public interface ReserveInventoryPort {
    void execute(String productId, String warehouseId, int quantity);
}
