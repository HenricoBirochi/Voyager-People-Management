package voyager.people_management.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyager.people_management.repositories.CargoRepository;
import voyager.people_management.repositories.FuncionarioRepository;
import voyager.people_management.models.Cargo;
import java.util.List;
import java.util.Optional;

@Service
public class CargoService {
    private final CargoRepository cargoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public CargoService(CargoRepository cargoRepository, FuncionarioRepository funcionarioRepository) {
        this.cargoRepository = cargoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public List<Cargo> findAll() {
        return cargoRepository.findAll();
    }

    public Optional<Cargo> findById(Long id) {
        return cargoRepository.findById(id);
    }

    @Transactional
    public Cargo save(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    @Transactional
    public void deleteById(Long id) {
        long count = funcionarioRepository.countByCargoId(id);
        if (count > 0) {
            throw new IllegalStateException("Não é possível excluir cargo que possui funcionários vinculados. Remova ou reatribua os funcionários antes de excluir.");
        }
        cargoRepository.deleteById(id);
    }
}
