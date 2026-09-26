package application.domain.exception;

public class DeliveryConfigRequiredException extends RuntimeException {
    public DeliveryConfigRequiredException(String productId) {
        super("Digital product " + productId + " must define how it will be delivered to the buyer");
    }
}
