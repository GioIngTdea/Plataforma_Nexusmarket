package application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Envio {
    private Pedido order;
    private OperadorLogistico handledBy;
}
