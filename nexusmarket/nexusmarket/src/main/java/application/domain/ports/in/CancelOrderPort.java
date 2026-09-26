package application.domain.ports.in;

public interface CancelOrderPort {
    void execute(String orderId, String requesterId);
}
