package voyager.people_management.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyager.people_management.repositories.ProjetoRepository;
import voyager.people_management.models.Projeto;
import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {
    private final ProjetoRepository projetoRepository;

    public ProjetoService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    public List<Projeto> findAll() {
        return projetoRepository.findAll();
    }

    public Optional<Projeto> findById(Long id) {
        return projetoRepository.findById(id);
    }

    @Transactional
    public Projeto save(Projeto projeto) {
        return projetoRepository.save(projeto);
    }

    @Transactional
    public void deleteById(Long id) {
        projetoRepository.deleteById(id);
    }
}
