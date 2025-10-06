package voyager.people_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import voyager.people_management.models.Departamento;
import voyager.people_management.repositories.DepartamentoRepository;

@Controller
public class DepartamentoController {

	private final DepartamentoRepository deptRepo;

	public DepartamentoController(DepartamentoRepository deptRepo) {
		this.deptRepo = deptRepo;
	}

	@GetMapping("/departamentos")
	public String list(Model model) {
		model.addAttribute("departamentos", deptRepo.findAll());
		return "/departamentos/list";
	}

	@GetMapping("/departamentos/new")
	public String form(Model model) {
		model.addAttribute("departamento", new Departamento());
		return "/departamentos/form";
	}

	@PostMapping("/departamentos/save")
	public String save(@Valid @ModelAttribute Departamento departamento, BindingResult br) {
		if (br.hasErrors()) return "departamentos/form";
		deptRepo.save(departamento);
		return "redirect:/departamentos";
	}

	@GetMapping("/departamentos/delete/{id}")
	public String delete(@PathVariable Integer id) {
		deptRepo.deleteById(id);
		return "redirect:/departamentos";
	}
}

