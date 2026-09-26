package application.domain.exception;

public class ReturnAlreadyResolvedException extends RuntimeException {
    public ReturnAlreadyResolvedException(String returnId) {
        super("Return request " + returnId + " was already resolved and cannot be modified");
    }
}
