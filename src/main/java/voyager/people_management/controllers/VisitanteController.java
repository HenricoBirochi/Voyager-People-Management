package voyager.people_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import voyager.people_management.models.Visitante;
import voyager.people_management.services.VisitanteService;
import voyager.people_management.services.DepartamentoService;
import voyager.people_management.services.FuncionarioService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class VisitanteController {

    private final VisitanteService visitanteService;
    private final DepartamentoService departamentoService;
    private final FuncionarioService funcionarioService;

    public VisitanteController(VisitanteService visitanteService, DepartamentoService departamentoService, FuncionarioService funcionarioService) {
        this.visitanteService = visitanteService;
        this.departamentoService = departamentoService;
        this.funcionarioService = funcionarioService;
    }

    @GetMapping("/visitantes")
    public String list(Model model) {
        model.addAttribute("visitantes", visitanteService.findAll());
        return "visitantes/list";
    }

    @GetMapping("/visitantes/new")
    public String form(Model model) {
        model.addAttribute("visitante", new Visitante());
        model.addAttribute("departamentos", departamentoService.findAll());
        model.addAttribute("funcionarios", funcionarioService.findAll());
        return "visitantes/form";
    }

    @PostMapping("/visitantes/save")
    public String save(@ModelAttribute Visitante v) {
        visitanteService.save(v);
        return "redirect:/visitantes";
    }

    @PostMapping("/visitantes/entrar")
    public String entrar(@ModelAttribute Visitante v) {
        visitanteService.registrarEntrada(v);
        return "redirect:/visitantes/cracha/" + v.getId();
    }

    @GetMapping("/visitantes/cracha/{id}")
    public String cracha(@PathVariable Long id, Model model) {
        var opt = visitanteService.findById(id);
        if (opt.isEmpty()) return "redirect:/visitantes";
        model.addAttribute("visitante", opt.get());
        return "visitantes/cracha";
    }

    @PostMapping("/visitantes/{id}/saida")
    public String saida(@PathVariable Long id) {
        visitanteService.registrarSaida(id);
        return "redirect:/visitantes";
    }

    @GetMapping("/visitantes/report")
    public String report(@RequestParam(required = false) String inicio, @RequestParam(required = false) String fim, @RequestParam(required = false) Long departamentoId, Model model) {
        List<Visitante> resultado = visitanteService.findAll();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (inicio != null && fim != null && !inicio.isBlank() && !fim.isBlank()) {
            var i = LocalDateTime.parse(inicio, fmt);
            var f = LocalDateTime.parse(fim, fmt);
            resultado = visitanteService.relatorioPeriodo(i, f);
        } else if (departamentoId != null) {
            resultado = visitanteService.relatorioPorDepartamento(departamentoId);
        }
        model.addAttribute("visitantes", resultado);
        model.addAttribute("departamentos", departamentoService.findAll());
        return "visitantes/report";
    }
}
