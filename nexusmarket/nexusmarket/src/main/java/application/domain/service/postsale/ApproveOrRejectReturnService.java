package application.domain.service.postsale;

import application.domain.exception.ReturnAlreadyResolvedException;
import application.domain.exception.ReturnNotFoundException;
import application.domain.exception.UnauthorizedUserException;
import application.domain.exception.UserNotFoundException;
import application.domain.models.Administrador;
import application.domain.models.Devolucion;
import application.domain.models.Producto;
import application.domain.models.Usuario;
import application.domain.models.Vendedor;
import application.domain.ports.in.ApproveOrRejectReturnPort;
import application.domain.ports.in.RestockReturnedInventoryPort;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueobjects.ReturnStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApproveOrRejectReturnService implements ApproveOrRejectReturnPort {

    private final ReturnRepositoryPort returnRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final RestockReturnedInventoryPort restockReturnedInventoryPort;

    @Override
    public Devolucion execute(String returnId, boolean approve, String resolverId) {
        Devolucion returnRequest = returnRepositoryPort.findById(returnId)
                .orElseThrow(() -> new ReturnNotFoundException(returnId));

        validatePending(returnRequest);
        Usuario resolver = validateResolver(returnRequest, resolverId);

        returnRequest.setStatus(approve ? ReturnStatus.APPROVED : ReturnStatus.REJECTED);
        returnRequest.setResolvedBy(resolver);

        Devolucion resolvedReturn = returnRepositoryPort.save(returnRequest);

        if (approve) {
            restockApprovedReturn(resolvedReturn);
        }

        return resolvedReturn;
    }

    private void validatePending(Devolucion returnRequest) {
        if (returnRequest.getStatus() != ReturnStatus.REQUESTED) {
            throw new ReturnAlreadyResolvedException(returnRequest.getId());
        }
    }

    private Usuario validateResolver(Devolucion returnRequest, String resolverId) {
        Usuario resolver = userRepositoryPort.findById(resolverId)
                .orElseThrow(() -> new UserNotFoundException(resolverId));

        Producto product = returnRequest.getProduct();
        boolean isOwningSeller = product.getPublishedBy() != null
                && product.getPublishedBy().getId().equals(resolverId);
        boolean isAdmin = resolver instanceof Administrador;

        if (!(resolver instanceof Vendedor && isOwningSeller) && !isAdmin) {
            throw new UnauthorizedUserException(resolverId,
                    "Only the owning seller or an administrator can resolve this return.");
        }

        return resolver;
    }

    private void restockApprovedReturn(Devolucion resolvedReturn) {
        String productId = resolvedReturn.getProduct().getId();
        String warehouseId = resolvedReturn.getOrder().getReservedWarehouseByProduct().get(productId);
        if (warehouseId == null) {
            return;
        }
        restockReturnedInventoryPort.execute(productId, warehouseId, 1);
    }
}
