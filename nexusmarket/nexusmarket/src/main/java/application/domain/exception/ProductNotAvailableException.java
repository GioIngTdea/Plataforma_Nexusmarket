package application.domain.exception;

public class ProductNotAvailableException extends RuntimeException {
    public ProductNotAvailableException(String productId) {
        super("Product " + productId + " is not published and cannot be purchased");
    }
}
