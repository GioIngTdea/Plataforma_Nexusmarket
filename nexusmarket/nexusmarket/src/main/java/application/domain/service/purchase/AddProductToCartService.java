package application.domain.service.purchase;

import application.domain.exception.ProductNotAvailableException;
import application.domain.exception.ProductNotFoundException;
import application.domain.exception.UserNotFoundException;
import application.domain.models.CarritoCompra;
import application.domain.models.Comprador;
import application.domain.models.Producto;
import application.domain.models.Usuario;
import application.domain.ports.in.AddProductToCartPort;
import application.domain.ports.out.CartRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddProductToCartService implements AddProductToCartPort {

    private final CartRepositoryPort cartRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public CarritoCompra execute(String buyerId, String productId, int quantity) {
        Comprador buyer = validateBuyer(buyerId);
        Producto product = validateAvailableProduct(productId);

        CarritoCompra cart = cartRepositoryPort.findByBuyerId(buyerId)
                .orElseGet(() -> newCartFor(buyer));

        cart.addProduct(product, quantity);

        return cartRepositoryPort.save(cart);
    }

    private Comprador validateBuyer(String buyerId) {
        Usuario user = userRepositoryPort.findById(buyerId)
                .orElseThrow(() -> new UserNotFoundException(buyerId));
        return (Comprador) user;
    }

    private Producto validateAvailableProduct(String productId) {
        Producto product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (!product.isAvailableForPurchase()) {
            throw new ProductNotAvailableException(productId);
        }

        return product;
    }

    private CarritoCompra newCartFor(Comprador buyer) {
        CarritoCompra cart = new CarritoCompra();
        cart.setOwner(buyer);
        return cart;
    }
}
