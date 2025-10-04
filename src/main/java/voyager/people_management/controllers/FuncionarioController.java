package voyager.people_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import voyager.people_management.models.Funcionario;
import voyager.people_management.repositories.CargoRepository;
import voyager.people_management.repositories.DepartamentoRepository;
import voyager.people_management.repositories.FuncionarioRepository;

@Controller
public class FuncionarioController {

	private final FuncionarioRepository funcRepo;
	private final CargoRepository cargoRepo;
	private final DepartamentoRepository deptRepo;

	public FuncionarioController(FuncionarioRepository funcRepo, CargoRepository cargoRepo, DepartamentoRepository deptRepo) {
		this.funcRepo = funcRepo;
		this.cargoRepo = cargoRepo;
		this.deptRepo = deptRepo;
	}

	@GetMapping("/funcionarios")
	public String list(Model model) {
		model.addAttribute("funcionarios", funcRepo.findAll());
		return "funcionarios/list";
	}

	@GetMapping("/funcionarios/new")
	public String form(Model model) {
		model.addAttribute("funcionario", new Funcionario());
		model.addAttribute("cargos", cargoRepo.findAll());
		model.addAttribute("departamentos", deptRepo.findAll());
		return "funcionarios/form";
	}

	@PostMapping("/funcionarios/save")
	public String save(@Valid @ModelAttribute Funcionario funcionario, BindingResult br, Integer cargoId, Integer departamentoId) {
		if (br.hasErrors()) return "funcionarios/form";
		if (cargoId != null) cargoRepo.findById(cargoId).ifPresent(funcionario::setCargo);
		if (departamentoId != null) deptRepo.findById(departamentoId).ifPresent(funcionario::setDepartamento);
		funcRepo.save(funcionario);
		return "redirect:/funcionarios";
	}

	@GetMapping("/funcionarios/delete/{id}")
	public String delete(@PathVariable Integer id) {
		funcRepo.deleteById(id);
		return "redirect:/funcionarios";
	}
}

