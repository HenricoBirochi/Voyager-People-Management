package voyager.people_management.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyager.people_management.models.Atividade;
import voyager.people_management.repositories.AtividadeRepository;
import java.util.List;
import java.util.Optional;

@Service
public class AtividadeService {
    private final AtividadeRepository repo;

    public AtividadeService(AtividadeRepository repo) {
        this.repo = repo;
    }

    public List<Atividade> findAll() { return repo.findAll(); }
    public Optional<Atividade> findById(Long id) { return repo.findById(id); }

    @Transactional
    public Atividade save(Atividade a) { return repo.save(a); }
}
