package application.domain.service.user;

import application.domain.exception.InvalidStatusTransitionException;
import application.domain.exception.UnauthorizedUserException;
import application.domain.exception.UserNotFoundException;
import application.domain.models.Administrador;
import application.domain.models.Usuario;
import application.domain.models.Vendedor;
import application.domain.ports.in.ChangeUserStatusPort;
import application.domain.ports.in.HideSellerProductsPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueobjects.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChangeUserStatusService implements ChangeUserStatusPort {

    private static final Set<UserStatus> ALLOWED_FROM_ACTIVE = Set.of(UserStatus.SUSPENDED, UserStatus.BLOCKED);
    private static final Set<UserStatus> ALLOWED_FROM_SUSPENDED = Set.of(UserStatus.ACTIVE, UserStatus.BLOCKED);

    private final UserRepositoryPort userRepositoryPort;
    private final HideSellerProductsPort hideSellerProductsPort;

    @Override
    public void execute(String userId, UserStatus newStatus, String adminId) {
        validateAdministrator(adminId);

        Usuario user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        validateTransition(user.getStatus(), newStatus);

        user.setStatus(newStatus);
        userRepositoryPort.save(user);

        if (user instanceof Vendedor && UserStatus.SUSPENDED.equals(newStatus)) {
            hideSellerProductsPort.execute(userId);
        }
    }

    private void validateAdministrator(String adminId) {
        Usuario admin = userRepositoryPort.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException(adminId));

        if (!(admin instanceof Administrador)) {
            throw new UnauthorizedUserException(adminId, "Only an administrator can change a user's status.");
        }
    }

    private void validateTransition(UserStatus current, UserStatus target) {
        boolean allowed = switch (current) {
            case ACTIVE -> ALLOWED_FROM_ACTIVE.contains(target);
            case SUSPENDED -> ALLOWED_FROM_SUSPENDED.contains(target);
            case BLOCKED -> false;
        };

        if (!allowed) {
            String reason = current == UserStatus.BLOCKED
                    ? "Blocked accounts are permanent and cannot transition to any other status."
                    : "This transition is not permitted by business rules.";
            throw new InvalidStatusTransitionException(current, target, reason);
        }
    }
}
