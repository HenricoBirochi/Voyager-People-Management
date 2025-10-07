package voyager.people_management.models;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "projetos")
@Getter
@Setter
public class Projeto extends AbstractEntity {

    @Column(nullable = false, length = 50)
    private String nome;

    @Column
    private String descricao;

    @ManyToMany
    private List<Funcionario> funcionarios;

    @ManyToOne
    @JoinColumn(name = "id_departamento_fk")
    private Departamento departamento;
}
