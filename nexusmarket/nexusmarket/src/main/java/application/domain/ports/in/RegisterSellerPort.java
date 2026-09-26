package application.domain.ports.in;

import application.domain.models.Vendedor;

public interface RegisterSellerPort {
    Vendedor execute(Vendedor seller, String adminId);
}
