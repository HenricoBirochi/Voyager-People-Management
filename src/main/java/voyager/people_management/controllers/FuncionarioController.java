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
import voyager.people_management.services.CargoHistoricoService;
import voyager.people_management.services.ProjetoService;
import java.util.stream.Collectors;
import java.util.List;
import voyager.people_management.services.PontoService;
import voyager.people_management.services.AtividadeService;
import voyager.people_management.models.Funcionario;
import voyager.people_management.models.Ponto;
import voyager.people_management.models.Atividade;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class FuncionarioController {

	private final FuncionarioService funcionarioService;
	private final CargoService cargoService;
	private final DepartamentoService departamentoService;
	private final PontoService pontoService;
	private final AtividadeService atividadeService;
	private final CargoHistoricoService cargoHistoricoService;
	private final ProjetoService projetoService;

	public FuncionarioController(FuncionarioService funcionarioService, CargoService cargoService, DepartamentoService departamentoService, PontoService pontoService, AtividadeService atividadeService, CargoHistoricoService cargoHistoricoService, ProjetoService projetoService) {
		this.funcionarioService = funcionarioService;
		this.cargoService = cargoService;
		this.departamentoService = departamentoService;
		this.pontoService = pontoService;
		this.atividadeService = atividadeService;
		this.cargoHistoricoService = cargoHistoricoService;
		this.projetoService = projetoService;
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
		model.addAttribute("projetos", projetoService.findAll());
		return "funcionarios/form";
	}

	@GetMapping("/funcionarios/edit/{id}")
	public String edit(@PathVariable Long id, Model model) {
		var funcOpt = funcionarioService.findById(id);
		if (funcOpt.isEmpty()) return "redirect:/funcionarios";
		model.addAttribute("funcionario", funcOpt.get());
		model.addAttribute("cargos", cargoService.findAll());
		model.addAttribute("departamentos", departamentoService.findAll());
		model.addAttribute("projetos", projetoService.findAll());
		return "funcionarios/form";
	}

	@PostMapping("/funcionarios/save")
	public String save(@Valid @ModelAttribute Funcionario funcionario, BindingResult br, Long cargoId, Long departamentoId, Long[] projetos, Model model) {
		if (cargoId != null) cargoService.findById(cargoId).ifPresent(funcionario::setCargo);
		if (departamentoId != null) departamentoService.findById(departamentoId).ifPresent(funcionario::setDepartamento);

		// handle projetos selection
		if (projetos != null) {
			List<voyager.people_management.models.Projeto> list = java.util.Arrays.stream(projetos)
					.map(id -> projetoService.findById(id).orElse(null))
					.filter(java.util.Objects::nonNull)
					.collect(Collectors.toList());
			funcionario.setProjetos(list);
		}

		// Record cargo history if cargo changed
		if (funcionario.getId() != null) {
			var existing = funcionarioService.findById(funcionario.getId());
			if (existing.isPresent()) {
				var ex = existing.get();
				if ((ex.getCargo() == null && funcionario.getCargo() != null) || (ex.getCargo() != null && funcionario.getCargo() != null && !ex.getCargo().getId().equals(funcionario.getCargo().getId()))) {
					// close previous history if exists
					var existingHist = cargoHistoricoService.findAll().stream()
							.filter(h -> h.getFuncionario() != null && h.getFuncionario().getId().equals(funcionario.getId()) && h.getDataFim() == null)
							.findFirst();
					if (existingHist.isPresent()) {
						var prev = existingHist.get();
						prev.setDataFim(java.time.LocalDate.now());
						cargoHistoricoService.save(prev);
					}

					var ch = new voyager.people_management.models.CargoHistorico();
					ch.setFuncionario(funcionario);
					ch.setCargo(funcionario.getCargo());
					ch.setDataInicio(java.time.LocalDate.now());
					cargoHistoricoService.save(ch);
				}
			}
		}

		// Validações customizadas
		if (funcionario.getDataDeNascimento() != null && funcionario.getDataDeContratacao() != null) {
			if (!funcionario.getDataDeNascimento().isBefore(funcionario.getDataDeContratacao())) {
				model.addAttribute("errorMessage", "A data de nascimento deve ser anterior à data de contratação.");
				model.addAttribute("cargos", cargoService.findAll());
				model.addAttribute("departamentos", departamentoService.findAll());
				model.addAttribute("projetos", projetoService.findAll());
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
				model.addAttribute("projetos", projetoService.findAll());
				return "funcionarios/form";
			}
		}
		if (br.hasErrors()) {
			model.addAttribute("cargos", cargoService.findAll());
			model.addAttribute("departamentos", departamentoService.findAll());
			model.addAttribute("projetos", projetoService.findAll());
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

	@PostMapping("/funcionarios/{id}/bater-ponto")
	public String baterPonto(@PathVariable Long id) {
		funcionarioService.findById(id).ifPresent(f -> funcionarioService.baterPonto(f, "ENTRADA"));
		return "redirect:/funcionarios";
	}

	@GetMapping("/funcionarios/{id}/atividade/new")
	public String atividadeForm(@PathVariable Long id, Model model) {
		var funcOpt = funcionarioService.findById(id);
		if (funcOpt.isEmpty()) return "redirect:/funcionarios";
		model.addAttribute("funcionario", funcOpt.get());
		return "funcionarios/atividade_form";
	}

	@PostMapping("/funcionarios/{id}/atividade/save")
	public String salvarAtividade(@PathVariable Long id, String inicio, String fim, String descricao) {
		var funcOpt = funcionarioService.findById(id);
		if (funcOpt.isEmpty()) return "redirect:/funcionarios";
		DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		LocalDateTime ini = LocalDateTime.parse(inicio, fmt);
		LocalDateTime fdt = LocalDateTime.parse(fim, fmt);
		funcionarioService.registrarAtividade(funcOpt.get(), ini, fdt, descricao);
		return "redirect:/funcionarios";
	}

	@GetMapping("/funcionarios/{id}/pontos")
	public String listPontos(@PathVariable Long id, Model model) {
		var funcOpt = funcionarioService.findById(id);
		if (funcOpt.isEmpty()) return "redirect:/funcionarios";
		model.addAttribute("funcionario", funcOpt.get());
		model.addAttribute("pontos", pontoService.findAll().stream().filter(p -> p.getFuncionario() != null && p.getFuncionario().getId().equals(id)).toList());
		return "funcionarios/pontos_list";
	}

	@GetMapping("/funcionarios/{id}/atividades")
	public String listAtividades(@PathVariable Long id, Model model) {
		var funcOpt = funcionarioService.findById(id);
		if (funcOpt.isEmpty()) return "redirect:/funcionarios";
		model.addAttribute("funcionario", funcOpt.get());
		model.addAttribute("atividades", atividadeService.findAll().stream().filter(a -> a.getFuncionario() != null && a.getFuncionario().getId().equals(id)).toList());
		return "funcionarios/atividades_list";
	}

	@GetMapping("/funcionarios/{id}/historico-cargos")
	public String listHistorico(@PathVariable Long id, Model model) {
		var funcOpt = funcionarioService.findById(id);
		if (funcOpt.isEmpty()) return "redirect:/funcionarios";
		model.addAttribute("funcionario", funcOpt.get());
		model.addAttribute("historicos", cargoHistoricoService.findAll().stream().filter(h -> h.getFuncionario() != null && h.getFuncionario().getId().equals(id)).toList());
		return "funcionarios/historico_list";
	}
}

