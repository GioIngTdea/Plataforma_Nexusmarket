package application.domain.models;

import application.domain.valueobjects.EstadoPedido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Pedido {
    private Comprador buyer;
    private CarritoCompra cart;
    private List<Producto> products = new ArrayList<>();
    private EstadoPedido estado;
}
