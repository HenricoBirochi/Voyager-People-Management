package voyager.people_management.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyager.people_management.models.TerceirizadoPonto;
import voyager.people_management.repositories.TerceirizadoPontoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class TerceirizadoPontoService {
    private final TerceirizadoPontoRepository repo;

    public TerceirizadoPontoService(TerceirizadoPontoRepository repo) {
        this.repo = repo;
    }

    public List<TerceirizadoPonto> findAll() { return repo.findAll(); }
    public Optional<TerceirizadoPonto> findById(Long id) { return repo.findById(id); }

    @Transactional
    public TerceirizadoPonto save(TerceirizadoPonto p) { return repo.save(p); }
}
