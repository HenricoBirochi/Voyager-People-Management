package voyager.people_management.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyager.people_management.models.Terceirizado;
import voyager.people_management.repositories.TerceirizadoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class TerceirizadoService {
    private final TerceirizadoRepository repo;

    public TerceirizadoService(TerceirizadoRepository repo) {
        this.repo = repo;
    }

    public List<Terceirizado> findAll() { return repo.findAll(); }
    public Optional<Terceirizado> findById(Long id) { return repo.findById(id); }

    @Transactional
    public Terceirizado save(Terceirizado t) { return repo.save(t); }
}
