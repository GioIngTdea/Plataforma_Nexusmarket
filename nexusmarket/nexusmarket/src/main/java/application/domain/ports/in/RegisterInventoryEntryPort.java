package application.domain.ports.in;

import application.domain.models.Inventario;

public interface RegisterInventoryEntryPort {
    Inventario execute(String productId, String warehouseId, int quantity, String requesterId);
}
