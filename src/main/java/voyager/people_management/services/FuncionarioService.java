package voyager.people_management.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyager.people_management.repositories.FuncionarioRepository;
import voyager.people_management.models.Funcionario;
import voyager.people_management.models.Ponto;
import voyager.people_management.models.Atividade;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    private final PontoService pontoService;
    private final AtividadeService atividadeService;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, PontoService pontoService, AtividadeService atividadeService) {
        this.funcionarioRepository = funcionarioRepository;
        this.pontoService = pontoService;
        this.atividadeService = atividadeService;
    }

    public List<Funcionario> findAll() {
        return funcionarioRepository.findAll();
    }

    /**
     * Find a funcionario by id.
     * @param id the funcionario id
     * @return optional funcionario
     */
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

    @Transactional
    public Ponto baterPonto(Funcionario f, String tipo) {
        Ponto p = new Ponto();
        p.setFuncionario(f);
        p.setHorario(LocalDateTime.now());
        p.setTipo(tipo);
        return pontoService.save(p);
    }

    @Transactional
    public Atividade registrarAtividade(Funcionario f, LocalDateTime inicio, LocalDateTime fim, String descricao) {
        Atividade a = new Atividade();
        a.setFuncionario(f);
        a.setInicio(inicio);
        a.setFim(fim);
        a.setDescricao(descricao);
        return atividadeService.save(a);
    }
}
