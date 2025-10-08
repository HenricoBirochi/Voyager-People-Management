package voyager.people_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import voyager.people_management.models.Atividade;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
}
