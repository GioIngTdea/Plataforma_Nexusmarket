package application.domain.exception;

public class MissingVariantException extends RuntimeException {
    public MissingVariantException(String productId) {
        super("Physical product " + productId + " must declare at least one variant");
    }
}
