package application.domain.ports.out;

import application.domain.models.Inventario;

import java.util.List;
import java.util.Optional;

/** Persists and retrieves per-warehouse product stock records. */
public interface InventoryRepositoryPort {
    Inventario save(Inventario inventory);
    Optional<Inventario> findByProductAndWarehouse(String productId, String warehouseId);
    /** All stock records for a product, across every warehouse that holds it. */
    List<Inventario> findByProduct(String productId);
}
