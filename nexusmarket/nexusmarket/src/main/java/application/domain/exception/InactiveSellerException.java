package application.domain.exception;

public class InactiveSellerException extends RuntimeException {
    public InactiveSellerException(String sellerId) {
        super("Seller " + sellerId + " is not active and cannot perform this operation");
    }
}
