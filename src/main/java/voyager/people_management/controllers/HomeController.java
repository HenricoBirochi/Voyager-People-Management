package voyager.people_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// ...existing code...
import voyager.people_management.repositories.CargoRepository;
import voyager.people_management.repositories.DepartamentoRepository;
import voyager.people_management.repositories.FuncionarioRepository;

// ...existing code...

@Controller
public class HomeController {

    private final CargoRepository cargoRepo;
    private final DepartamentoRepository deptRepo;
    private final FuncionarioRepository funcRepo;

    public HomeController(CargoRepository cargoRepo, DepartamentoRepository deptRepo, FuncionarioRepository funcRepo) {
        this.cargoRepo = cargoRepo;
        this.deptRepo = deptRepo;
        this.funcRepo = funcRepo;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        long cargosCount = cargoRepo.count();
        long departamentosCount = deptRepo.count();
        long funcionariosCount = funcRepo.count();

        model.addAttribute("cargosCount", cargosCount);
        model.addAttribute("departamentosCount", departamentosCount);
        model.addAttribute("funcionariosCount", funcionariosCount);

        return "index";
    }
}
