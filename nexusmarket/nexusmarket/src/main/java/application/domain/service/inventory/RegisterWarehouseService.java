package application.domain.service.inventory;

import application.domain.exception.UnauthorizedUserException;
import application.domain.exception.UserNotFoundException;
import application.domain.models.Administrador;
import application.domain.models.Bodega;
import application.domain.models.Usuario;
import application.domain.ports.in.RegisterWarehousePort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.ports.out.WarehouseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterWarehouseService implements RegisterWarehousePort {

    private final WarehouseRepositoryPort warehouseRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Bodega execute(Bodega warehouse, String adminId) {
        Administrador administrator = validateAdministrator(adminId);
        warehouse.setRegisteredBy(administrator);
        return warehouseRepositoryPort.save(warehouse);
    }

    private Administrador validateAdministrator(String adminId) {
        Usuario admin = userRepositoryPort.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException(adminId));

        if (!(admin instanceof Administrador administrator)) {
            throw new UnauthorizedUserException(adminId, "Only an administrator can register a warehouse.");
        }

        return administrator;
    }
}
