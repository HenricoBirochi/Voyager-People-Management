package voyager.people_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import voyager.people_management.models.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
