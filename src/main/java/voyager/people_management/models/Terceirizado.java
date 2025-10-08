package voyager.people_management.models;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "terceirizados")
@Getter
@Setter
@NoArgsConstructor
public class Terceirizado extends AbstractEntity {
    @Column(nullable = false, length = 60)
    private String nome;

    @Column(nullable = false, length = 14)
    private String cpf;

    @Column(length = 100)
    private String funcao;

    @Column(length = 120)
    private String empresaPrestadora;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    @ManyToOne
    @JoinColumn(name = "responsavel_interno_id")
    private Funcionario responsavelInterno;

    @ManyToMany
    private List<Departamento> departamentos;
}
