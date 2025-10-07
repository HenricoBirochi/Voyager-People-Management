package voyager.people_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import voyager.people_management.models.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
