package application.domain.ports.out;

import application.domain.models.CarritoCompra;

import java.util.Optional;

/** Persists and retrieves shopping carts. */
public interface CartRepositoryPort {
    CarritoCompra save(CarritoCompra cart);
    Optional<CarritoCompra> findById(String id);
    Optional<CarritoCompra> findByBuyerId(String buyerId);
}
