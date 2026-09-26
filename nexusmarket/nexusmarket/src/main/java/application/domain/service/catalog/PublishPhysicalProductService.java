package application.domain.service.catalog;

import application.domain.exception.InactiveSellerException;
import application.domain.exception.MissingVariantException;
import application.domain.exception.UserNotFoundException;
import application.domain.models.ProductoFisico;
import application.domain.models.Usuario;
import application.domain.models.Vendedor;
import application.domain.ports.in.PublishPhysicalProductPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublishPhysicalProductService implements PublishPhysicalProductPort {

    private final ProductRepositoryPort productRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public ProductoFisico execute(ProductoFisico product) {
        Vendedor seller = validateActiveSeller(product.getPublishedBy().getId());
        validateHasVariant(product);

        product.setPublishedBy(seller);
        product.publish();

        return (ProductoFisico) productRepositoryPort.save(product);
    }

    private Vendedor validateActiveSeller(String sellerId) {
        Usuario seller = userRepositoryPort.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException(sellerId));

        if (!(seller instanceof Vendedor vendedor) || !seller.isActive()) {
            throw new InactiveSellerException(sellerId);
        }

        return vendedor;
    }

    private void validateHasVariant(ProductoFisico product) {
        if (product.getVariants() == null || product.getVariants().isEmpty()) {
            throw new MissingVariantException(product.getId());
        }
    }
}
