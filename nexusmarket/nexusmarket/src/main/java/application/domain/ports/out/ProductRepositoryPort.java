package application.domain.ports.out;

import application.domain.models.Producto;

import java.util.List;
import java.util.Optional;

/** Persists and retrieves catalog products. */
public interface ProductRepositoryPort {
    Producto save(Producto product);
    Optional<Producto> findById(String id);
    List<Producto> findBySeller(String sellerId);
}
