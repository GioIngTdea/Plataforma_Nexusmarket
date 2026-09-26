package application.domain.service.catalog;

import application.domain.models.Producto;
import application.domain.ports.in.HideSellerProductsPort;
import application.domain.ports.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HideSellerProductsService implements HideSellerProductsPort {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public void execute(String sellerId) {
        for (Producto product : productRepositoryPort.findBySeller(sellerId)) {
            product.suspend();
            productRepositoryPort.save(product);
        }
    }
}
