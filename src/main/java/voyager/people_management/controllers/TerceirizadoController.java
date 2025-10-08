package voyager.people_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import voyager.people_management.models.Terceirizado;
import voyager.people_management.services.TerceirizadoService;
import voyager.people_management.services.TerceirizadoPontoService;
import voyager.people_management.services.DepartamentoService;
import voyager.people_management.services.FuncionarioService;
import java.time.LocalDate;

@Controller
public class TerceirizadoController {
	private final TerceirizadoService terceirizadoService;
	private final TerceirizadoPontoService pontoService;
	private final DepartamentoService departamentoService;
	private final FuncionarioService funcionarioService;

	public TerceirizadoController(TerceirizadoService terceirizadoService, TerceirizadoPontoService pontoService, DepartamentoService departamentoService, FuncionarioService funcionarioService) {
		this.terceirizadoService = terceirizadoService;
		this.pontoService = pontoService;
		this.departamentoService = departamentoService;
		this.funcionarioService = funcionarioService;
	}

	@GetMapping("/terceirizados")
	public String list(Model model) {
		model.addAttribute("terceirizados", terceirizadoService.findAll());
		return "terceirizados/list";
	}

	@GetMapping("/terceirizados/new")
	public String form(Model model) {
		model.addAttribute("terceirizado", new Terceirizado());
		model.addAttribute("departamentos", departamentoService.findAll());
		model.addAttribute("funcionarios", funcionarioService.findAll());
		return "terceirizados/form";
	}

	@PostMapping("/terceirizados/save")
	public String save(@ModelAttribute Terceirizado t) {
		terceirizadoService.save(t);
		return "redirect:/terceirizados";
	}

	@GetMapping("/terceirizados/{id}/pontos")
	public String pontos(@PathVariable Long id, Model model) {
		var opt = terceirizadoService.findById(id);
		if (opt.isEmpty()) return "redirect:/terceirizados";
		model.addAttribute("terceirizado", opt.get());
		model.addAttribute("pontos", pontoService.findAll().stream().filter(p -> p.getTerceirizado() != null && p.getTerceirizado().getId().equals(id)).toList());
		return "terceirizados/pontos_list";
	}

	@PostMapping("/terceirizados/{id}/renovar")
	public String renovar(@PathVariable Long id, String novoFim) {
		var opt = terceirizadoService.findById(id);
		if (opt.isEmpty()) return "redirect:/terceirizados";
		var t = opt.get();
		if (novoFim != null && !novoFim.isBlank()) {
			t.setDataFim(LocalDate.parse(novoFim));
		}
		terceirizadoService.save(t);
		return "redirect:/terceirizados";
	}
}
