package voyager.people_management.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cargos")
@Getter
@Setter
@NoArgsConstructor
public class Cargo extends AbstractEntity {
    
    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private Double salario;

    @OneToMany(mappedBy = "cargo", cascade = jakarta.persistence.CascadeType.REMOVE)
    private List<Funcionario> funcionarios;
}
