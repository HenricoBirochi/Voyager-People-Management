package voyager.people_management.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyager.people_management.repositories.CargoRepository;
import voyager.people_management.models.Cargo;
import java.util.List;
import java.util.Optional;

@Service
public class CargoService {
    private final CargoRepository cargoRepository;

    public CargoService(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
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
        cargoRepository.deleteById(id);
    }
}
