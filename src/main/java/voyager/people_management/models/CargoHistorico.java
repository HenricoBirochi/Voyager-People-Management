package voyager.people_management.models;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cargos_historico")
@Getter
@Setter
@NoArgsConstructor
public class CargoHistorico extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    private LocalDate dataInicio;
    private LocalDate dataFim;
}
