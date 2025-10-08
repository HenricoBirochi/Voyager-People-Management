package voyager.people_management.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import voyager.people_management.models.Visitante;
import voyager.people_management.repositories.VisitanteRepository;

@Service
public class VisitanteService {

    private final VisitanteRepository repo;

    public VisitanteService(VisitanteRepository repo) {
        this.repo = repo;
    }

    /**
     * Save or update a visitante. If the visitante does not have a crachá code,
     * a new one will be generated.
     *
     * @param v Visitante entity to save
     * @return persisted Visitante
     */
    public Visitante save(Visitante v) {
        if (v.getCrachaCodigo() == null || v.getCrachaCodigo().isBlank()) {
            v.setCrachaCodigo(generateCracha());
        }
        return repo.save(v);
    }

    public Optional<Visitante> findById(Long id) {
        return repo.findById(id);
    }

    public List<Visitante> findAll() {
        return repo.findAll();
    }

    public Visitante registrarEntrada(Visitante v) {
        v.setDataEntrada(LocalDateTime.now());
        if (v.getCrachaCodigo() == null || v.getCrachaCodigo().isBlank()) v.setCrachaCodigo(generateCracha());
        return repo.save(v);
    }

    public Visitante registrarSaida(Long id) {
        var opt = repo.findById(id);
        if (opt.isEmpty()) return null;
        var v = opt.get();
        v.setDataSaida(LocalDateTime.now());
        return repo.save(v);
    }

    public List<Visitante> relatorioPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return repo.findByDataEntradaBetween(inicio, fim);
    }

    public List<Visitante> relatorioPorDepartamento(Long departamentoId) {
        return repo.findByDepartamentoVisitadoId(departamentoId);
    }

    public List<Visitante> buscarPorNome(String nome) {
        return repo.findByNomeCompletoContainingIgnoreCase(nome);
    }

    private String generateCracha() {
        // simple crachá generator: timestamp + random short
        return "CR-" + System.currentTimeMillis() + "-" + (int)(Math.random()*900 + 100);
    }
}
