package application.domain.ports.in;

import application.domain.models.ProductoFisico;

public interface PublishPhysicalProductPort {
    ProductoFisico execute(ProductoFisico product);
}
