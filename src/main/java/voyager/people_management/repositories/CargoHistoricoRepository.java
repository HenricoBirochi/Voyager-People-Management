package voyager.people_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import voyager.people_management.models.CargoHistorico;

public interface CargoHistoricoRepository extends JpaRepository<CargoHistorico, Long> {
}
