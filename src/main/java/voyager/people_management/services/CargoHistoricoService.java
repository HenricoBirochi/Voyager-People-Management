package voyager.people_management.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import voyager.people_management.models.CargoHistorico;
import voyager.people_management.repositories.CargoHistoricoRepository;
import java.util.List;
import java.util.Optional;

@Service
public class CargoHistoricoService {
    private final CargoHistoricoRepository repo;

    public CargoHistoricoService(CargoHistoricoRepository repo) {
        this.repo = repo;
    }

    public List<CargoHistorico> findAll() { return repo.findAll(); }
    public Optional<CargoHistorico> findById(Long id) { return repo.findById(id); }

    @Transactional
    public CargoHistorico save(CargoHistorico ch) { return repo.save(ch); }
}
