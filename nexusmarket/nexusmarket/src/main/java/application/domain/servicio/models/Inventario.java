package application.domain.models;

import application.domain.valueobjects.TipoMovimientoInventario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Inventario {
    private Producto product;
    private Bodega warehouse;
    private TipoMovimientoInventario movementType;
    private int stock;
}
