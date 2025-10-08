package voyager.people_management.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyager.people_management.models.Ponto;
import voyager.people_management.repositories.PontoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class PontoService {
    private final PontoRepository repo;

    public PontoService(PontoRepository repo) {
        this.repo = repo;
    }

    public List<Ponto> findAll() { return repo.findAll(); }
    public Optional<Ponto> findById(Long id) { return repo.findById(id); }

    @Transactional
    public Ponto save(Ponto p) { return repo.save(p); }
}
