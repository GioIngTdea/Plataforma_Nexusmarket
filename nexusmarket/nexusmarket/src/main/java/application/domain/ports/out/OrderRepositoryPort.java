package application.domain.ports.out;

import application.domain.models.Pedido;

import java.util.Optional;

/** Persists and retrieves orders. */
public interface OrderRepositoryPort {
    Pedido save(Pedido order);
    Optional<Pedido> findById(String id);
}
