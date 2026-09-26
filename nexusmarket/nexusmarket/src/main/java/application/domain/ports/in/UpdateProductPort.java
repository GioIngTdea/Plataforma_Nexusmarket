package application.domain.ports.in;

import application.domain.models.Producto;

public interface UpdateProductPort {
    Producto execute(Producto updatedData, String requesterId);
}
