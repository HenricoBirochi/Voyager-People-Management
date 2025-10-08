package voyager.people_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import voyager.people_management.models.Terceirizado;

public interface TerceirizadoRepository extends JpaRepository<Terceirizado, Long> {
}
