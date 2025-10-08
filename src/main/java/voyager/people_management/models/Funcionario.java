package voyager.people_management.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "funcionarios")
@Getter
@Setter
@NoArgsConstructor
public class Funcionario extends AbstractEntity {

    @Column(nullable = false, unique = true, length = 60)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false)
    private LocalDate dataDeNascimento;

    @Column(nullable = false)
    private LocalDate dataDeContratacao;

    @ManyToOne
    @JoinColumn(name = "id_cargo_fk")
    private Cargo cargo;

    @ManyToOne
    @JoinColumn(name = "id_departamento_fk")
    private Departamento departamento;

    @ManyToMany
    @jakarta.persistence.JoinTable(name = "funcionario_projeto",
        joinColumns = @jakarta.persistence.JoinColumn(name = "funcionario_id"),
        inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "projeto_id"))
    private List<Projeto> projetos;

    // convenience methods can be added in the future (addProjeto/removeProjeto)
}
