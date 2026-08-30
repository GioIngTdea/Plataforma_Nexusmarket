package application.domain.models;

import application.domain.valueobjects.TipoBodega;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Bodega {
    private TipoBodega tipo;
    private String owner;
    private Administrador registeredBy;
}
