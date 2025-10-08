package voyager.people_management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import voyager.people_management.models.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
	long countByCargoId(Long cargoId);
}
