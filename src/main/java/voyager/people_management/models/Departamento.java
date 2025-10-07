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
@Table(name = "departamentos")
@Getter
@Setter
@NoArgsConstructor
public class Departamento extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String nome;

    @OneToMany(mappedBy = "departamento", cascade = jakarta.persistence.CascadeType.REMOVE)
    private List<Funcionario> funcionarios;
}
