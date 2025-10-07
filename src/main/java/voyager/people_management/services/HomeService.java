package voyager.people_management.services;

import org.springframework.stereotype.Service;
import voyager.people_management.repositories.DepartamentoRepository;
import voyager.people_management.repositories.CargoRepository;
import voyager.people_management.repositories.FuncionarioRepository;

@Service
public class HomeService {
    private final CargoRepository cargoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public HomeService(CargoRepository cargoRepository, DepartamentoRepository departamentoRepository, FuncionarioRepository funcionarioRepository) {
        this.cargoRepository = cargoRepository;
        this.departamentoRepository = departamentoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public long countCargos() {
        return cargoRepository.count();
    }
    public long countDepartamentos() {
        return departamentoRepository.count();
    }
    public long countFuncionarios() {
        return funcionarioRepository.count();
    }
}
