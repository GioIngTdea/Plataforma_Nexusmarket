package application.domain.exception;

public class ProductOwnershipException extends RuntimeException {
    public ProductOwnershipException(String productId, String userId) {
        super("User " + userId + " is not the owner of product " + productId + " nor an administrator");
    }
}
