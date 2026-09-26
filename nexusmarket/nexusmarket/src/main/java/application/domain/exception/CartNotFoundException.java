package application.domain.exception;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String cartId) {
        super("No shopping cart found with id: " + cartId);
    }
}
