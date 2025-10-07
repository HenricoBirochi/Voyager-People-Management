package voyager.people_management.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyager.people_management.repositories.DepartamentoRepository;
import voyager.people_management.models.Departamento;
import java.util.List;
import java.util.Optional;

@Service
public class DepartamentoService {
    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public List<Departamento> findAll() {
        return departamentoRepository.findAll();
    }

    public Optional<Departamento> findById(Long id) {
        return departamentoRepository.findById(id);
    }

    @Transactional
    public Departamento save(Departamento departamento) {
        return departamentoRepository.save(departamento);
    }

    @Transactional
    public void deleteById(Long id) {
        departamentoRepository.deleteById(id);
    }
}
