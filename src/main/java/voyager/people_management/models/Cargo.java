package voyager.people_management.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cargos")
public class Cargo extends AbstractEntity {
    
    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private Double salario;

    @OneToMany(mappedBy = "cargo")
    private List<Funcionario> funcionarios;
}
