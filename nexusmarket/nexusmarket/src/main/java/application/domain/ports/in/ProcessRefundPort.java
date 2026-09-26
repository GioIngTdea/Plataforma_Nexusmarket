package application.domain.ports.in;

import application.domain.models.Reembolso;

public interface ProcessRefundPort {
    Reembolso execute(String returnId, String adminId);
}
