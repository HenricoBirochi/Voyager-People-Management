package voyager.people_management.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Visitante extends AbstractEntity {

    private String nomeCompleto;

    private String documento;

    private String motivo;

    private LocalDateTime dataEntrada;

    private LocalDateTime dataSaida;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionarioVisitado;

    @ManyToOne
    @JoinColumn(name = "departamento_id")
    private Departamento departamentoVisitado;

    @Column(unique = true)
    private String crachaCodigo;
}
