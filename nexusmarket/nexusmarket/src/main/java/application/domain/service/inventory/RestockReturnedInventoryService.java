package application.domain.service.inventory;

import application.domain.exception.InventoryNotFoundException;
import application.domain.models.Inventario;
import application.domain.ports.in.RestockReturnedInventoryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.valueobjects.InventoryMovementType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestockReturnedInventoryService implements RestockReturnedInventoryPort {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    @Override
    public void execute(String productId, String warehouseId, int quantity) {
        Inventario inventory = inventoryRepositoryPort.findByProductAndWarehouse(productId, warehouseId)
                .orElseThrow(() -> new InventoryNotFoundException(productId, warehouseId));

        inventory.increaseStock(quantity, InventoryMovementType.RETURN);
        inventoryRepositoryPort.save(inventory);
    }
}
