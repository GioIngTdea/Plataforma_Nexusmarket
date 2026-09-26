package application.domain.ports.out;

import application.domain.models.Bodega;

import java.util.Optional;

/** Persists and retrieves warehouses. */
public interface WarehouseRepositoryPort {
    Bodega save(Bodega warehouse);
    Optional<Bodega> findById(String id);
}
