package application.domain.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super("No order found with id: " + orderId);
    }
}
