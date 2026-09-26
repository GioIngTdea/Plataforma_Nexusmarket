package application.domain.service.postsale;

import application.domain.exception.RefundAlreadyProcessedException;
import application.domain.exception.ReturnNotApprovedException;
import application.domain.exception.ReturnNotFoundException;
import application.domain.exception.UnauthorizedUserException;
import application.domain.exception.UserNotFoundException;
import application.domain.models.Administrador;
import application.domain.models.Devolucion;
import application.domain.models.Reembolso;
import application.domain.models.Usuario;
import application.domain.ports.in.ProcessRefundPort;
import application.domain.ports.out.RefundRepositoryPort;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.ports.out.UserRepositoryPort;
import application.domain.valueobjects.RefundStatus;
import application.domain.valueobjects.ReturnStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProcessRefundService implements ProcessRefundPort {

    private final ReturnRepositoryPort returnRepositoryPort;
    private final RefundRepositoryPort refundRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Reembolso execute(String returnId, String adminId) {
        Devolucion returnRequest = returnRepositoryPort.findById(returnId)
                .orElseThrow(() -> new ReturnNotFoundException(returnId));

        Administrador admin = validateAdministrator(adminId);
        validateApproved(returnRequest);
        validateNotAlreadyRefunded(returnId);

        Reembolso refund = new Reembolso();
        refund.setDevolucion(returnRequest);
        refund.setAmount(resolveRefundAmount(returnRequest));
        refund.setStatus(RefundStatus.PROCESSED);
        refund.setProcessedBy(admin);

        return refundRepositoryPort.save(refund);
    }

    private Administrador validateAdministrator(String adminId) {
        Usuario admin = userRepositoryPort.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException(adminId));

        if (!(admin instanceof Administrador administrator)) {
            throw new UnauthorizedUserException(adminId, "Only an administrator can process a refund.");
        }

        return administrator;
    }

    private void validateApproved(Devolucion returnRequest) {
        if (returnRequest.getStatus() != ReturnStatus.APPROVED) {
            throw new ReturnNotApprovedException(returnRequest.getId());
        }
    }

    private void validateNotAlreadyRefunded(String returnId) {
        if (refundRepositoryPort.findByReturnId(returnId).isPresent()) {
            throw new RefundAlreadyProcessedException(returnId);
        }
    }

    private BigDecimal resolveRefundAmount(Devolucion returnRequest) {
        return returnRequest.getProduct().getPrice();
    }
}
