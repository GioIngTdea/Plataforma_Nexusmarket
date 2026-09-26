package application.domain.exception;

public class CartNotOwnedByBuyerException extends RuntimeException {
    public CartNotOwnedByBuyerException(String cartId, String buyerId) {
        super("Cart " + cartId + " does not belong to buyer " + buyerId);
    }
}
