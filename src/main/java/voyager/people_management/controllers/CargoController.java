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
import voyager.people_management.repositories.CargoRepository;

@Controller
public class CargoController {

	private final CargoRepository cargoRepo;

	public CargoController(CargoRepository cargoRepo) {
		this.cargoRepo = cargoRepo;
	}

	@GetMapping("/cargos")
	public String list(Model model) {
		model.addAttribute("cargos", cargoRepo.findAll());
		return "cargos/list";
	}

	@GetMapping("/cargos/new")
	public String form(Model model) {
		model.addAttribute("cargo", new Cargo());
		return "cargos/form";
	}

	@PostMapping("/cargos/save")
	public String save(@Valid @ModelAttribute Cargo cargo, BindingResult br) {
		if (br.hasErrors()) return "cargos/form";
		cargoRepo.save(cargo);
		return "redirect:/cargos";
	}

	@GetMapping("/cargos/delete/{id}")
	public String delete(@PathVariable Integer id) {
		cargoRepo.deleteById(id);
		return "redirect:/cargos";
	}
}
