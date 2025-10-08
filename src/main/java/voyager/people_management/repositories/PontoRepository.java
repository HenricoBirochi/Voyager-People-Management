package voyager.people_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import voyager.people_management.models.Ponto;

public interface PontoRepository extends JpaRepository<Ponto, Long> {
}
