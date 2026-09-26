package application.domain.service.purchase;

import application.domain.exception.CartNotFoundException;
import application.domain.exception.CartNotOwnedByBuyerException;
import application.domain.exception.ProductNotFoundException;
import application.domain.models.CarritoCompra;
import application.domain.models.Producto;
import application.domain.ports.in.RemoveProductFromCartPort;
import application.domain.ports.out.CartRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveProductFromCartService implements RemoveProductFromCartPort {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public CarritoCompra execute(String buyerId, String productId) {
        CarritoCompra cart = cartRepositoryPort.findByBuyerId(buyerId)
                .orElseThrow(() -> new CartNotFoundException(buyerId));

        validateOwnership(cart, buyerId);

        Producto product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        cart.removeProduct(product);

        return cartRepositoryPort.save(cart);
    }

    private void validateOwnership(CarritoCompra cart, String buyerId) {
        if (!cart.getOwner().getId().equals(buyerId)) {
            throw new CartNotOwnedByBuyerException(cart.getId(), buyerId);
        }
    }
}
