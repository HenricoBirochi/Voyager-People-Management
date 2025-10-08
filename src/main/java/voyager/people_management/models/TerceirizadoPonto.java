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
@Table(name = "terceirizados_pontos")
@Getter
@Setter
@NoArgsConstructor
public class TerceirizadoPonto extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "terceirizado_id")
    private Terceirizado terceirizado;

    private LocalDateTime horario;

    private String tipo; // ENTRADA/SAIDA
}
