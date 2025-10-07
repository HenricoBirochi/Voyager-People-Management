package voyager.people_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import voyager.people_management.models.Cargo;
import voyager.people_management.services.CargoService;

@Controller
public class CargoController {

	private final CargoService cargoService;

	public CargoController(CargoService cargoService) {
		this.cargoService = cargoService;
	}

	@GetMapping("/cargos")
	public String list(Model model) {
		model.addAttribute("cargos", cargoService.findAll());
		return "/cargos/list";
	}


	@GetMapping("/cargos/new")
	public String form(Model model) {
		model.addAttribute("cargo", new Cargo());
		return "/cargos/form";
	}

	@GetMapping("/cargos/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		var cargo = cargoService.findById(id).orElseThrow();
		model.addAttribute("cargo", cargo);
		return "/cargos/form";
	}

	@PostMapping("/cargos/save")
	public String save(@Valid @ModelAttribute Cargo cargo, BindingResult br, Model model) {
		// Validação de unicidade do nome
		var all = cargoService.findAll();
		boolean nomeDuplicado = all.stream().anyMatch(c -> c.getNome().equalsIgnoreCase(cargo.getNome()) && (cargo.getId() == null || !c.getId().equals(cargo.getId())));
		if (nomeDuplicado) {
			br.rejectValue("nome", "error.cargo", "Já existe um cargo com este nome.");
		}
		if (br.hasErrors()) {
			model.addAttribute("cargo", cargo);
			return "cargos/form";
		}
		cargoService.save(cargo);
		return "redirect:/cargos";
	}

	@GetMapping("/cargos/delete/{id}")
	public String delete(@PathVariable Long id) {
		cargoService.deleteById(id);
		return "redirect:/cargos";
	}
}
