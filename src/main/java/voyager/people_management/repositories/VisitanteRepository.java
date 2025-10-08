package voyager.people_management.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import voyager.people_management.models.Visitante;

public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
    List<Visitante> findByDataEntradaBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Visitante> findByDepartamentoVisitadoId(Long departamentoId);
    List<Visitante> findByNomeCompletoContainingIgnoreCase(String nome);
}
