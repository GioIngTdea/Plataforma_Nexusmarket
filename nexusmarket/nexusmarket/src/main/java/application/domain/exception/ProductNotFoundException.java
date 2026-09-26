package application.domain.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productId) {
        super("No product found with id: " + productId);
    }
}
