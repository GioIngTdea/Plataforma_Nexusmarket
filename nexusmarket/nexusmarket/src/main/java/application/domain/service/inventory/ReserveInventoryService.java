package application.domain.service.inventory;

import application.domain.exception.InsufficientInventoryException;
import application.domain.exception.InventoryNotFoundException;
import application.domain.models.Inventario;
import application.domain.ports.in.ReserveInventoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.valueobjects.InventoryMovementType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReserveInventoryService implements ReserveInventoryPort {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public void execute(String productId, String warehouseId, int quantity) {
        Inventario inventory = inventoryRepositoryPort.findByProductAndWarehouse(productId, warehouseId)
                .orElseThrow(() -> new InventoryNotFoundException(productId, warehouseId));

        if (inventory.getStock() < quantity) {
            throw new InsufficientInventoryException(productId, quantity, inventory.getStock());
        }

        inventory.decreaseStock(quantity, InventoryMovementType.RESERVATION);
        inventoryRepositoryPort.save(inventory);
    }
}
