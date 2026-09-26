package application.domain.exception;

public class ReturnNotApprovedException extends RuntimeException {
    public ReturnNotApprovedException(String returnId) {
        super("Return request " + returnId + " must be approved before a refund can be processed");
    }
}
