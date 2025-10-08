package voyager.people_management.models;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "atividades")
@Getter
@Setter
@NoArgsConstructor
public class Atividade extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    private LocalDateTime inicio;

    private LocalDateTime fim;

    @Column(length = 500)
    private String descricao;
}
