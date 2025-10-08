package voyager.people_management.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import voyager.people_management.services.FuncionarioService;
import voyager.people_management.services.TerceirizadoService;
import voyager.people_management.services.VisitanteService;
import voyager.people_management.services.DepartamentoService;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import voyager.people_management.models.Visitante;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

@Controller
public class ReportsController {

    private final FuncionarioService funcionarioService;
    private final TerceirizadoService terceirizadoService;
    private final VisitanteService visitanteService;
    private final DepartamentoService departamentoService;

    public ReportsController(FuncionarioService funcionarioService, TerceirizadoService terceirizadoService, VisitanteService visitanteService, DepartamentoService departamentoService) {
        this.funcionarioService = funcionarioService;
        this.terceirizadoService = terceirizadoService;
        this.visitanteService = visitanteService;
        this.departamentoService = departamentoService;
    }

    @GetMapping("/reports/employees")
    public String employeesBy(Model model, @RequestParam(required = false) Long departamentoId, @RequestParam(required = false) Long cargoId, @RequestParam(required = false) Long terceirizadaId) {
        // simple filters - for now use full lists and template filtering
        model.addAttribute("funcionarios", funcionarioService.findAll());
        model.addAttribute("departamentos", departamentoService.findAll());
        model.addAttribute("terceirizados", terceirizadoService.findAll());
        return "reports/employees";
    }

    @GetMapping("/reports/visitantes")
    public String visitantesReport(Model model, @RequestParam(required = false) String inicio, @RequestParam(required = false) String fim) {
        model.addAttribute("departamentos", departamentoService.findAll());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (inicio != null && fim != null && !inicio.isBlank() && !fim.isBlank()) {
            var i = LocalDateTime.parse(inicio, fmt);
            var f = LocalDateTime.parse(fim, fmt);
            model.addAttribute("visitantes", visitanteService.relatorioPeriodo(i, f));
        } else {
            model.addAttribute("visitantes", visitanteService.findAll());
        }
        return "reports/visitantes";
    }

    @GetMapping("/reports/daily")
    public String dailyCirculation(Model model) {
        // simplistic daily circulation: today's visitors + today's activities counts
        var hojeInicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        var hojeFim = hojeInicio.plusDays(1);
        model.addAttribute("visitantesHoje", visitanteService.relatorioPeriodo(hojeInicio, hojeFim));
        model.addAttribute("funcionarios", funcionarioService.findAll());
        model.addAttribute("terceirizados", terceirizadoService.findAll());
        return "reports/daily";
    }

    @GetMapping("/reports/employees/export.csv")
    @ResponseBody
    public ResponseEntity<byte[]> exportEmployeesCsv(@RequestParam(required = false) Long departamentoId, @RequestParam(required = false) Long cargoId, @RequestParam(required = false) Long terceirizadaId) throws Exception {
        var list = funcionarioService.findAll();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (var writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8)) {
            writer.write("nome,departamento,cargo,projetos\n");
            for (var f : list) {
                String dept = f.getDepartamento() != null ? f.getDepartamento().getNome() : "";
                String cargo = f.getCargo() != null ? f.getCargo().getNome() : "";
                String projetos = f.getProjetos() != null ? String.join(";", f.getProjetos().stream().map(p -> p.getNome()).toList()) : "";
                writer.write(String.format("\"%s\",%s,%s,%s\n", f.getNome(), dept, cargo, projetos));
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employees.csv");
        return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
    }

    @GetMapping("/reports/employees/export.pdf")
    @ResponseBody
    public ResponseEntity<byte[]> exportEmployeesPdf(@RequestParam(required = false) Long departamentoId, @RequestParam(required = false) Long cargoId, @RequestParam(required = false) Long terceirizadaId) throws Exception {
        var list = funcionarioService.findAll();

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
                cs.newLineAtOffset(50, 750);
                cs.showText("Relatório de Funcionários");
                cs.newLineAtOffset(0, -20);
                cs.setFont(PDType1Font.HELVETICA, 10);
                for (var f : list) {
                    String line = String.format("%s | %s | %s", f.getNome(), f.getDepartamento() != null ? f.getDepartamento().getNome() : "", f.getCargo() != null ? f.getCargo().getNome() : "");
                    cs.showText(line);
                    cs.newLineAtOffset(0, -14);
                }
                cs.endText();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employees.pdf");
            return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
        }
    }

    @GetMapping("/reports/daily/export.csv")
    @ResponseBody
    public ResponseEntity<byte[]> exportDailyCsv() throws Exception {
        // produce a simple CSV with today's visitors and counts
        var hojeInicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        var hojeFim = hojeInicio.plusDays(1);
        var visitantes = visitanteService.relatorioPeriodo(hojeInicio, hojeFim);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (var writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8)) {
            writer.write("tipo,nome,entrada,saida,destino\n");
            for (var v : visitantes) {
                String destino = v.getFuncionarioVisitado() != null ? v.getFuncionarioVisitado().getNome() : (v.getDepartamentoVisitado() != null ? v.getDepartamentoVisitado().getNome() : "");
                writer.write(String.format("visitante,\"%s\",%s,%s,%s\n", v.getNomeCompleto(), v.getDataEntrada(), v.getDataSaida(), destino));
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily.csv");
        return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
    }

    @GetMapping("/reports/daily/export.pdf")
    @ResponseBody
    public ResponseEntity<byte[]> exportDailyPdf() throws Exception {
        var hojeInicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        var hojeFim = hojeInicio.plusDays(1);
        var visitantes = visitanteService.relatorioPeriodo(hojeInicio, hojeFim);

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
                cs.newLineAtOffset(50, 750);
                cs.showText("Relatório Diário");
                cs.newLineAtOffset(0, -20);
                cs.setFont(PDType1Font.HELVETICA, 10);
                for (var v : visitantes) {
                    String line = String.format("%s | %s | %s", v.getNomeCompleto(), v.getDataEntrada(), v.getDataSaida());
                    cs.showText(line);
                    cs.newLineAtOffset(0, -14);
                }
                cs.endText();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily.pdf");
            return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
        }
    }

    @GetMapping("/reports/visitantes/export.csv")
    @ResponseBody
    public ResponseEntity<byte[]> exportVisitantesCsv(@RequestParam(required = false) String inicio, @RequestParam(required = false) String fim) throws Exception {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        List<Visitante> list;
        if (inicio != null && fim != null && !inicio.isBlank() && !fim.isBlank()) {
            var i = LocalDateTime.parse(inicio, fmt);
            var f = LocalDateTime.parse(fim, fmt);
            list = visitanteService.relatorioPeriodo(i, f);
        } else {
            list = visitanteService.findAll();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (var writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8)) {
            writer.write("nome,documento,motivo,entrada,saida,destino,cracha\n");
            for (var v : list) {
                String destino = v.getFuncionarioVisitado() != null ? v.getFuncionarioVisitado().getNome() : (v.getDepartamentoVisitado() != null ? v.getDepartamentoVisitado().getNome() : "");
                writer.write(String.format("\"%s\",%s,%s,%s,%s,%s,%s\n", v.getNomeCompleto(), v.getDocumento(), v.getMotivo(), v.getDataEntrada(), v.getDataSaida(), destino, v.getCrachaCodigo()));
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=visitantes.csv");
        return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
    }

    @GetMapping("/reports/visitantes/export.pdf")
    @ResponseBody
    public ResponseEntity<byte[]> exportVisitantesPdf(@RequestParam(required = false) String inicio, @RequestParam(required = false) String fim) throws Exception {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        List<Visitante> list;
        if (inicio != null && fim != null && !inicio.isBlank() && !fim.isBlank()) {
            var i = LocalDateTime.parse(inicio, fmt);
            var f = LocalDateTime.parse(fim, fmt);
            list = visitanteService.relatorioPeriodo(i, f);
        } else {
            list = visitanteService.findAll();
        }

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
                cs.newLineAtOffset(50, 750);
                cs.showText("Relatório de Visitantes");
                cs.newLineAtOffset(0, -20);
                cs.setFont(PDType1Font.HELVETICA, 10);
                for (var v : list) {
                    String line = String.format("%s | %s | %s | %s", v.getNomeCompleto(), v.getDocumento(), v.getDataEntrada(), v.getDataSaida());
                    cs.showText(line);
                    cs.newLineAtOffset(0, -14);
                }
                cs.endText();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=visitantes.pdf");
            return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
        }
    }
}
