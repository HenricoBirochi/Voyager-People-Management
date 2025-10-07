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
import voyager.people_management.services.FuncionarioService;
import voyager.people_management.services.CargoService;
import voyager.people_management.services.DepartamentoService;

@Controller
public class FuncionarioController {

	private final FuncionarioService funcionarioService;
	private final CargoService cargoService;
	private final DepartamentoService departamentoService;

	public FuncionarioController(FuncionarioService funcionarioService, CargoService cargoService, DepartamentoService departamentoService) {
		this.funcionarioService = funcionarioService;
		this.cargoService = cargoService;
		this.departamentoService = departamentoService;
	}

	@GetMapping("/funcionarios")
	public String list(Model model) {
		model.addAttribute("funcionarios", funcionarioService.findAll());
		return "funcionarios/list";
	}

	@GetMapping("/funcionarios/new")
	public String form(Model model) {
		model.addAttribute("funcionario", new Funcionario());
		model.addAttribute("cargos", cargoService.findAll());
		model.addAttribute("departamentos", departamentoService.findAll());
		return "funcionarios/form";
	}

	@PostMapping("/funcionarios/save")
	public String save(@Valid @ModelAttribute Funcionario funcionario, BindingResult br, Long cargoId, Long departamentoId, Model model) {
		if (cargoId != null) cargoService.findById(cargoId).ifPresent(funcionario::setCargo);
		if (departamentoId != null) departamentoService.findById(departamentoId).ifPresent(funcionario::setDepartamento);

		// Validações customizadas
		if (funcionario.getDataDeNascimento() != null && funcionario.getDataDeContratacao() != null) {
			if (!funcionario.getDataDeNascimento().isBefore(funcionario.getDataDeContratacao())) {
				model.addAttribute("errorMessage", "A data de nascimento deve ser anterior à data de contratação.");
				model.addAttribute("cargos", cargoService.findAll());
				model.addAttribute("departamentos", departamentoService.findAll());
				return "funcionarios/form";
			}
			if (funcionario.getDataDeNascimento().isAfter(java.time.LocalDate.now().minusYears(16))) {
				model.addAttribute("errorMessage", "O funcionário deve ter pelo menos 16 anos de idade.");
				model.addAttribute("cargos", cargoService.findAll());
				model.addAttribute("departamentos", departamentoService.findAll());
				return "funcionarios/form";
			}
			if (funcionario.getDataDeContratacao().isAfter(java.time.LocalDate.now())) {
				model.addAttribute("errorMessage", "A data de contratação não pode ser maior que hoje.");
				model.addAttribute("cargos", cargoService.findAll());
				model.addAttribute("departamentos", departamentoService.findAll());
				return "funcionarios/form";
			}
		}
		if (br.hasErrors()) {
			model.addAttribute("cargos", cargoService.findAll());
			model.addAttribute("departamentos", departamentoService.findAll());
			return "funcionarios/form";
		}
		funcionarioService.save(funcionario);
		return "redirect:/funcionarios";
	}

	@GetMapping("/funcionarios/delete/{id}")
	public String delete(@PathVariable Long id) {
		funcionarioService.deleteById(id);
		return "redirect:/funcionarios";
	}
}

