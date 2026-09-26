package application.domain.ports.in;

import application.domain.models.ProductoDigital;

public interface PublishDigitalProductPort {
    ProductoDigital execute(ProductoDigital product);
}
