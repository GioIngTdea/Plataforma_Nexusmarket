package application.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Comprador extends Usuario {
    private String primaryAddress;
    private List<String> additionalAddresses = new ArrayList<>();
    private String commercialStatus;
}
