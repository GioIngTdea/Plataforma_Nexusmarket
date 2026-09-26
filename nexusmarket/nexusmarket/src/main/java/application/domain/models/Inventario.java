package application.domain.models;

import application.domain.exception.NegativeStockException;
import application.domain.valueobjects.InventoryMovementType;
import lombok.Getter;
import lombok.Setter;

/**
 * The available stock of a product at a specific warehouse. Inventory is
 * distributed and must always be linked to both a product and a warehouse.
 * Negative stock is never allowed.
 */
@Getter
@Setter
public class Inventario {
    private String id;
    private Producto product;
    private Bodega warehouse;
    private InventoryMovementType movementType;
    private int stock;

    public void increaseStock(int quantity, InventoryMovementType reason) {
        this.stock += quantity;
        this.movementType = reason;
    }

    public void decreaseStock(int quantity, InventoryMovementType reason) {
        if (quantity > this.stock) {
            throw new NegativeStockException(product.getId(), warehouse.getId(), stock, quantity);
        }
        this.stock -= quantity;
        this.movementType = reason;
    }
}
