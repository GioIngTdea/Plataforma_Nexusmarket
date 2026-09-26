package application.domain.service.catalog;

import application.domain.exception.ProductNotFoundException;
import application.domain.exception.ProductOwnershipException;
import application.domain.exception.UserNotFoundException;
import application.domain.models.Administrador;
import application.domain.models.Producto;
import application.domain.models.Usuario;
import application.domain.ports.in.UpdateProductPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProductService implements UpdateProductPort {

    private final ProductRepositoryPort productRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Producto execute(Producto updatedData, String requesterId) {
        Producto product = productRepositoryPort.findById(updatedData.getId())
                .orElseThrow(() -> new ProductNotFoundException(updatedData.getId()));

        validateOwnershipOrAdmin(product, requesterId);

        product.setName(updatedData.getName());
        product.setDescription(updatedData.getDescription());
        product.setPrice(updatedData.getPrice());
        product.setVariants(updatedData.getVariants());
        product.setStatus(updatedData.getStatus());

        return productRepositoryPort.save(product);
    }

    private void validateOwnershipOrAdmin(Producto product, String requesterId) {
        Usuario requester = userRepositoryPort.findById(requesterId)
                .orElseThrow(() -> new UserNotFoundException(requesterId));

        boolean isOwner = product.getPublishedBy() != null
                && product.getPublishedBy().getId().equals(requesterId);
        boolean isAdmin = requester instanceof Administrador;

        if (!isOwner && !isAdmin) {
            throw new ProductOwnershipException(product.getId(), requesterId);
        }
    }
}
