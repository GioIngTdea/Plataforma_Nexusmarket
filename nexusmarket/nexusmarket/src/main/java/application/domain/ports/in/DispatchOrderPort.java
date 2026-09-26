package application.domain.ports.in;

import application.domain.models.Envio;

public interface DispatchOrderPort {
    Envio execute(String orderId, String logisticsOperatorId);
}
