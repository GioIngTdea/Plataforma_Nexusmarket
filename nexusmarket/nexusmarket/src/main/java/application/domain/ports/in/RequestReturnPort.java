package application.domain.ports.in;

import application.domain.models.Devolucion;

public interface RequestReturnPort {
    Devolucion execute(String orderId, String productId, String buyerId, String reason);
}
