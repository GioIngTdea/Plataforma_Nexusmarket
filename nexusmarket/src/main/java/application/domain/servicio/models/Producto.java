package application.domain.models;

import application.domain.valueobjects.EstadoProducto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Producto {
    private List<String> variantes = new ArrayList<>();
    private EstadoProducto status;
    private Vendedor publishedBy;
}
