package application.domain.service.user;

import application.domain.exception.DuplicateEmailException;
import application.domain.exception.DuplicateIdException;
import application.domain.exception.UnauthorizedUserException;
import application.domain.exception.UserNotFoundException;
import application.domain.models.Administrador;
import application.domain.models.Bodega;
import application.domain.models.Usuario;
import application.domain.models.Vendedor;
import application.domain.ports.in.RegisterSellerPort;
import application.domain.ports.in.RegisterWarehousePort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueobjects.Role;
import application.domain.valueobjects.UserStatus;
import application.domain.valueobjects.WarehouseOwnerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterSellerService implements RegisterSellerPort {

    private final UserRepositoryPort userRepositoryPort;
    private final RegisterWarehousePort registerWarehousePort;

    @Override
    public Vendedor execute(Vendedor seller, String adminId) {
        Administrador administrator = validateAdministrator(adminId);
        validateUniqueEmail(seller.getEmail());
        validateUniqueIdentifier(seller.getId());

        seller.setRole(Role.SELLER);
        seller.setStatus(UserStatus.ACTIVE);
        seller.setRegisteredBy(administrator);

        Vendedor persistedSeller = (Vendedor) userRepositoryPort.save(seller);

        Bodega initialWarehouse = new Bodega();
        initialWarehouse.setOwnerType(WarehouseOwnerType.SELLER);
        initialWarehouse.setOwnerSeller(persistedSeller);
        registerWarehousePort.execute(initialWarehouse, adminId);

        return persistedSeller;
    }

    private Administrador validateAdministrator(String adminId) {
        Usuario admin = userRepositoryPort.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException(adminId));

        if (!(admin instanceof Administrador administrator)) {
            throw new UnauthorizedUserException(adminId, "Only an administrator can register a seller.");
        }

        return administrator;
    }

    private void validateUniqueEmail(String email) {
        if (userRepositoryPort.findByEmail(email).isPresent()) {
            throw new DuplicateEmailException(email);
        }
    }

    private void validateUniqueIdentifier(String identifier) {
        if (userRepositoryPort.findByIdentifier(identifier).isPresent()) {
            throw new DuplicateIdException(identifier);
        }
    }
}
