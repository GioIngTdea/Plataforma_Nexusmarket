package application.domain.exception;

public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException(String productId, int requested, int available) {
        super("Insufficient stock for product " + productId + ": requested " + requested + ", available " + available);
    }
}
