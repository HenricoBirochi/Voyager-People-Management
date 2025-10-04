package voyager.people_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import voyager.people_management.models.Cargo;
import voyager.people_management.models.Departamento;
import voyager.people_management.models.Funcionario;
import voyager.people_management.repositories.CargoRepository;
import voyager.people_management.repositories.DepartamentoRepository;
import voyager.people_management.repositories.FuncionarioRepository;

import java.util.List;

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
        List<Cargo> cargos = cargoRepo.findAll();
        List<Departamento> departamentos = deptRepo.findAll();
        List<Funcionario> funcionarios = funcRepo.findAll();

        model.addAttribute("cargos", cargos);
        model.addAttribute("departamentos", departamentos);
        model.addAttribute("funcionarios", funcionarios);

        model.addAttribute("cargo", new Cargo());
        model.addAttribute("departamento", new Departamento());
        model.addAttribute("funcionario", new Funcionario());

        return "index";
    }

    @PostMapping("/cargo")
    public String addCargo(@ModelAttribute Cargo cargo) {
        cargoRepo.save(cargo);
        return "redirect:/";
    }

    @PostMapping("/departamento")
    public String addDepartamento(@ModelAttribute Departamento departamento) {
        deptRepo.save(departamento);
        return "redirect:/";
    }

    @PostMapping("/funcionario")
    public String addFuncionario(@ModelAttribute Funcionario funcionario, Integer cargoId, Integer departamentoId) {
        if (cargoId != null) {
            cargoRepo.findById(cargoId).ifPresent(funcionario::setCargo);
        }
        if (departamentoId != null) {
            deptRepo.findById(departamentoId).ifPresent(funcionario::setDepartamento);
        }
        funcRepo.save(funcionario);
        return "redirect:/";
    }
}
