package application.domain.ports.in;

import application.domain.models.Devolucion;

public interface ApproveOrRejectReturnPort {
    Devolucion execute(String returnId, boolean approve, String resolverId);
}
