package application.domain.ports.in;

import application.domain.models.Bodega;

public interface RegisterWarehousePort {
    Bodega execute(Bodega warehouse, String adminId);
}
