package voyager.people_management.models;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pontos")
@Getter
@Setter
@NoArgsConstructor
public class Ponto extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    private LocalDateTime horario;

    private String tipo; // e.g., ENTRADA, SAIDA
}
