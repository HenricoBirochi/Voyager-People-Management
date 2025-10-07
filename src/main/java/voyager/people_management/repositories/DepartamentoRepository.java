package voyager.people_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import voyager.people_management.models.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
}
