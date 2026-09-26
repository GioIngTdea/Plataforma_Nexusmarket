package application.domain.service.inventory;

import application.domain.exception.InventoryNotFoundException;
import application.domain.models.Inventario;
import application.domain.ports.in.AdjustInventoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.valueobjects.InventoryMovementType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdjustInventoryService implements AdjustInventoryPort {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public Inventario execute(String productId, String warehouseId, int quantity,
                               InventoryMovementType reason, String requesterId) {
        Inventario inventory = inventoryRepositoryPort.findByProductAndWarehouse(productId, warehouseId)
                .orElseThrow(() -> new InventoryNotFoundException(productId, warehouseId));

        if (reason == InventoryMovementType.ENTRY || reason == InventoryMovementType.RETURN) {
            inventory.increaseStock(quantity, reason);
        } else {
            inventory.decreaseStock(quantity, reason);
        }

        return inventoryRepositoryPort.save(inventory);
    }
}
