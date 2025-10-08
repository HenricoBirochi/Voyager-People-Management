package voyager.people_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import voyager.people_management.models.Projeto;
import voyager.people_management.services.ProjetoService;
import voyager.people_management.services.DepartamentoService;
import voyager.people_management.services.FuncionarioService;

@Controller
public class ProjetoController {

    private final ProjetoService projetoService;
    private final DepartamentoService departamentoService;
    private final FuncionarioService funcionarioService;

    public ProjetoController(ProjetoService projetoService, DepartamentoService departamentoService, FuncionarioService funcionarioService) {
        this.projetoService = projetoService;
        this.departamentoService = departamentoService;
        this.funcionarioService = funcionarioService;
    }

    @GetMapping("/projetos")
    public String list(Model model) {
        model.addAttribute("projetos", projetoService.findAll());
        return "projetos/list";
    }


    @GetMapping("/projetos/new")
    public String form(Model model) {
        model.addAttribute("projeto", new Projeto());
        model.addAttribute("departamentos", departamentoService.findAll());
        model.addAttribute("funcionarios", funcionarioService.findAll());
        return "projetos/form";
    }

    @GetMapping("/projetos/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var projeto = projetoService.findById(id).orElseThrow();
        model.addAttribute("projeto", projeto);
        model.addAttribute("departamentos", departamentoService.findAll());
        model.addAttribute("funcionarios", funcionarioService.findAll());
        return "projetos/form";
    }

    @GetMapping("/projetos/view/{id}")
    public String view(@PathVariable Long id) {
        // Some templates/linkers used a /view/{id} URL but we don't have a separate view page.
        // Redirect to the edit page to avoid 404s.
        return "redirect:/projetos/edit/" + id;
    }

    @PostMapping("/projetos/save")
    public String save(@Valid @ModelAttribute Projeto projeto, BindingResult br, Long departamentoId, Long[] funcionarioIds, Model model) {
        // Validação de unicidade do nome
        var all = projetoService.findAll();
        boolean nomeDuplicado = all.stream().anyMatch(p -> p.getNome().equalsIgnoreCase(projeto.getNome()) && (projeto.getId() == null || !p.getId().equals(projeto.getId())));
        if (nomeDuplicado) {
            br.rejectValue("nome", "error.projeto", "Já existe um projeto com este nome.");
        }
        if (departamentoId != null) departamentoService.findById(departamentoId).ifPresent(projeto::setDepartamento);
        if (funcionarioIds != null) {
            java.util.Set<Long> idSet = java.util.Arrays.stream(funcionarioIds).map(Long::valueOf).collect(java.util.stream.Collectors.toSet());
            var funcionarios = funcionarioService.findAll().stream()
                .filter(f -> idSet.contains(f.getId() != null ? f.getId().longValue() : null))
                .toList();
            projeto.setFuncionarios(funcionarios);
        }
        if (br.hasErrors()) {
            model.addAttribute("departamentos", departamentoService.findAll());
            model.addAttribute("funcionarios", funcionarioService.findAll());
            return "projetos/form";
        }
        projetoService.save(projeto);
        return "redirect:/projetos";
    }

    @GetMapping("/projetos/delete/{id}")
    public String delete(@PathVariable Long id) {
        projetoService.deleteById(id);
        return "redirect:/projetos";
    }
}
