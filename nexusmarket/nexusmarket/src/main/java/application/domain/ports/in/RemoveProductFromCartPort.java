package application.domain.ports.in;

import application.domain.models.CarritoCompra;

public interface RemoveProductFromCartPort {
    CarritoCompra execute(String buyerId, String productId);
}
