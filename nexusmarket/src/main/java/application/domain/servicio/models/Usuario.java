package application.domain.models;

import application.domain.valueobjects.Rol;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Usuario {
    private String id;
    private String fullName;
    private String email;
    private Rol role;
    private String status;
}
