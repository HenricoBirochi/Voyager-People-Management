package voyager.people_management.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import voyager.people_management.models.Funcionario;
import voyager.people_management.models.Ponto;
import voyager.people_management.models.Atividade;
import voyager.people_management.repositories.FuncionarioRepository;

class FuncionarioServiceTest {

    private FuncionarioRepository fr;
    private PontoService pontoService;
    private AtividadeService atividadeService;
    private FuncionarioService service;

    @BeforeEach
    void setup() {
        fr = Mockito.mock(FuncionarioRepository.class);
        pontoService = Mockito.mock(PontoService.class);
        atividadeService = Mockito.mock(AtividadeService.class);
        service = new FuncionarioService(fr, pontoService, atividadeService);
    }

    @Test
    void baterPontoCreatesPonto() {
        Funcionario f = new Funcionario();
        f.setId(2L);
        when(pontoService.save(any(Ponto.class))).thenAnswer(i -> i.getArgument(0));

        Ponto p = service.baterPonto(f, "ENTRADA");
        assertNotNull(p.getHorario());
        assertEquals(f, p.getFuncionario());
        assertEquals("ENTRADA", p.getTipo());
    }

    @Test
    void registrarAtividadeCreatesAtividade() {
        Funcionario f = new Funcionario();
        f.setId(3L);
        LocalDateTime inicio = LocalDateTime.now().minusHours(1);
        LocalDateTime fim = LocalDateTime.now();
        when(atividadeService.save(any(Atividade.class))).thenAnswer(i -> i.getArgument(0));

        Atividade a = service.registrarAtividade(f, inicio, fim, "Reunião");
        assertEquals(f, a.getFuncionario());
        assertEquals("Reunião", a.getDescricao());
    }
}
