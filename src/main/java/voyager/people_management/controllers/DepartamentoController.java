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
import voyager.people_management.services.DepartamentoService;

@Controller
public class DepartamentoController {

	private final DepartamentoService departamentoService;

	public DepartamentoController(DepartamentoService departamentoService) {
		this.departamentoService = departamentoService;
	}

	@GetMapping("/departamentos")
	public String list(Model model) {
		model.addAttribute("departamentos", departamentoService.findAll());
		return "/departamentos/list";
	}


	@GetMapping("/departamentos/new")
	public String form(Model model) {
		model.addAttribute("departamento", new Departamento());
		return "/departamentos/form";
	}

	@GetMapping("/departamentos/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		var departamento = departamentoService.findById(id).orElseThrow();
		model.addAttribute("departamento", departamento);
		return "/departamentos/form";
	}

	@PostMapping("/departamentos/save")
	public String save(@Valid @ModelAttribute Departamento departamento, BindingResult br, Model model) {
		// Validação de unicidade do nome
		var all = departamentoService.findAll();
		boolean nomeDuplicado = all.stream().anyMatch(d -> d.getNome().equalsIgnoreCase(departamento.getNome()) && (departamento.getId() == null || !d.getId().equals(departamento.getId())));
		if (nomeDuplicado) {
			br.rejectValue("nome", "error.departamento", "Já existe um departamento com este nome.");
		}
		if (br.hasErrors()) {
			model.addAttribute("departamento", departamento);
			return "departamentos/form";
		}
		departamentoService.save(departamento);
		return "redirect:/departamentos";
	}

	@GetMapping("/departamentos/delete/{id}")
	public String delete(@PathVariable Long id) {
		departamentoService.deleteById(id);
		return "redirect:/departamentos";
	}
}

