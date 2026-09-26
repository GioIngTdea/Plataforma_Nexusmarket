package application.domain.ports.in;

public interface CompleteDeliveryPort {
    void execute(String orderId);
}
