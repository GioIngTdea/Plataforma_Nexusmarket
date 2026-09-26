package application.domain.service.user;

import application.domain.exception.DuplicateEmailException;
import application.domain.exception.DuplicateIdException;
import application.domain.models.Comprador;
import application.domain.ports.in.RegisterBuyerPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueobjects.Role;
import application.domain.valueobjects.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterBuyerService implements RegisterBuyerPort {

    private static final String DEFAULT_COMMERCIAL_STATUS = "NORMAL";

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Comprador execute(Comprador buyer) {
        validateUniqueEmail(buyer.getEmail());
        validateUniqueIdentifier(buyer.getId());

        buyer.setRole(Role.BUYER);
        buyer.setStatus(UserStatus.ACTIVE);
        buyer.setCommercialStatus(DEFAULT_COMMERCIAL_STATUS);

        return (Comprador) userRepositoryPort.save(buyer);
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
