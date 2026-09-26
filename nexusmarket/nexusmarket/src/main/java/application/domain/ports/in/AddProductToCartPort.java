package application.domain.ports.in;

import application.domain.models.CarritoCompra;

public interface AddProductToCartPort {
    CarritoCompra execute(String buyerId, String productId, int quantity);
}
