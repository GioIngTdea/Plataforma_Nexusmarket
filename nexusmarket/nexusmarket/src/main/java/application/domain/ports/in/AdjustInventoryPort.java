package application.domain.ports.in;

import application.domain.models.Inventario;
import application.domain.valueobjects.InventoryMovementType;

public interface AdjustInventoryPort {
    Inventario execute(String productId, String warehouseId, int quantity,
                        InventoryMovementType reason, String requesterId);
}
