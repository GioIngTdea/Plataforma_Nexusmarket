package application.domain.service.inventory;

import application.domain.exception.ProductNotFoundException;
import application.domain.exception.WarehouseNotFoundException;
import application.domain.models.Bodega;
import application.domain.models.Inventario;
import application.domain.models.Producto;
import application.domain.ports.in.RegisterInventoryEntryPort;
import application.domain.ports.out.InventoryRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.WarehouseRepositoryPort;
import application.domain.valueobjects.InventoryMovementType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterInventoryEntryService implements RegisterInventoryEntryPort {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final WarehouseRepositoryPort warehouseRepositoryPort;

    @Override
    public Inventario execute(String productId, String warehouseId, int quantity, String requesterId) {
        Producto product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        Bodega warehouse = warehouseRepositoryPort.findById(warehouseId)
                .orElseThrow(() -> new WarehouseNotFoundException(warehouseId));

        Inventario inventory = inventoryRepositoryPort.findByProductAndWarehouse(productId, warehouseId)
                .orElseGet(() -> newInventoryRecord(product, warehouse));

        inventory.increaseStock(quantity, InventoryMovementType.ENTRY);

        return inventoryRepositoryPort.save(inventory);
    }

    private Inventario newInventoryRecord(Producto product, Bodega warehouse) {
        Inventario inventory = new Inventario();
        inventory.setProduct(product);
        inventory.setWarehouse(warehouse);
        inventory.setStock(0);
        return inventory;
    }
}
