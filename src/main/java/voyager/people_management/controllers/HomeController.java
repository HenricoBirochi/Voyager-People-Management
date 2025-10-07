package voyager.people_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
// ...existing code...
import voyager.people_management.services.HomeService;

// ...existing code...

@Controller
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("cargosCount", homeService.countCargos());
        model.addAttribute("departamentosCount", homeService.countDepartamentos());
        model.addAttribute("funcionariosCount", homeService.countFuncionarios());
        return "/index";
    }
}
