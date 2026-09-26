package application.domain.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException(String cartId) {
        super("Cart " + cartId + " is empty and cannot be confirmed");
    }
}
