package application.domain.service.purchase;

import application.domain.exception.OrderNotFoundException;
import application.domain.exception.ProductNotFoundException;
import application.domain.exception.ReturnNotEligibleException;
import application.domain.exception.UnauthorizedUserException;
import application.domain.models.Devolucion;
import application.domain.models.Pedido;
import application.domain.models.Producto;
import application.domain.ports.in.RequestReturnPort;
import application.domain.ports.out.OrderRepositoryPort;
import application.domain.ports.out.ProductRepositoryPort;
import application.domain.ports.out.ReturnRepositoryPort;
import application.domain.valueobjects.OrderStatus;
import application.domain.valueobjects.ReturnStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestReturnService implements RequestReturnPort {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final ReturnRepositoryPort returnRepositoryPort;

    @Override
    public Devolucion execute(String orderId, String productId, String buyerId, String reason) {
        Pedido order = orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        validateBuyerOwnsOrder(order, buyerId);
        validateOrderIsCompleted(order);

        Producto product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Devolucion returnRequest = new Devolucion();
        returnRequest.setOrder(order);
        returnRequest.setProduct(product);
        returnRequest.setRequestedBy(order.getBuyer());
        returnRequest.setReason(reason);
        returnRequest.setStatus(ReturnStatus.REQUESTED);

        return returnRepositoryPort.save(returnRequest);
    }

    private void validateBuyerOwnsOrder(Pedido order, String buyerId) {
        if (!order.getBuyer().getId().equals(buyerId)) {
            throw new UnauthorizedUserException(buyerId, "This order does not belong to the requesting buyer.");
        }
    }

    private void validateOrderIsCompleted(Pedido order) {
        if (order.getStatus() != OrderStatus.DELIVERED_COMPLETED) {
            throw new ReturnNotEligibleException(order.getId());
        }
    }
}
