package application.domain.service.catalog;

import application.domain.exception.DeliveryConfigRequiredException;
import application.domain.exception.InactiveSellerException;
import application.domain.exception.UserNotFoundException;
import application.domain.models.ProductoDigital;
import application.domain.models.Usuario;
import application.domain.models.Vendedor;
import application.domain.ports.in.PublishDigitalProductPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishDigitalProductService implements PublishDigitalProductPort {

    private final ProductRepositoryPort productRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public ProductoDigital execute(ProductoDigital product) {
        Vendedor seller = validateActiveSeller(product.getPublishedBy().getId());
        validateDeliveryConfig(product);

        product.setPublishedBy(seller);
        product.publish();

        return (ProductoDigital) productRepositoryPort.save(product);
    }

    private Vendedor validateActiveSeller(String sellerId) {
        Usuario seller = userRepositoryPort.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException(sellerId));

        if (!(seller instanceof Vendedor vendedor) || !seller.isActive()) {
            throw new InactiveSellerException(sellerId);
        }

        return vendedor;
    }

    private void validateDeliveryConfig(ProductoDigital product) {
        if (product.getDeliveryConfig() == null || product.getDeliveryConfig().isBlank()) {
            throw new DeliveryConfigRequiredException(product.getId());
        }
    }
}
