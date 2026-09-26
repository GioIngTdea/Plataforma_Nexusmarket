package application.domain.ports.in;

import application.domain.models.Pedido;

public interface ConfirmOrderPort {
    Pedido execute(String buyerId, String deliveryAddress);
}
