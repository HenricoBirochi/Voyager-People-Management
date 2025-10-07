package voyager.people_management.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyager.people_management.repositories.FuncionarioRepository;
import voyager.people_management.models.Funcionario;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    public Optional<Funcionario> findById(Long id) {
        return funcionarioRepository.findById(id);
    }

    @Transactional
    public Funcionario save(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }

    @Transactional
    public void deleteById(Long id) {
        funcionarioRepository.deleteById(id);
    }
}
