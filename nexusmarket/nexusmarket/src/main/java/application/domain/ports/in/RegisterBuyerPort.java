package application.domain.ports.in;

import application.domain.models.Comprador;

public interface RegisterBuyerPort {
    Comprador execute(Comprador buyer);
}
