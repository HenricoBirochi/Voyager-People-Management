package voyager.people_management.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import voyager.people_management.models.Visitante;
import voyager.people_management.repositories.VisitanteRepository;

class VisitanteServiceTest {

    private VisitanteRepository repo;
    private VisitanteService service;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(VisitanteRepository.class);
        service = new VisitanteService(repo);
    }

    @Test
    void saveGeneratesCrachaIfMissing() {
        Visitante v = new Visitante();
        v.setNomeCompleto("João");
        v.setDocumento("123");
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        Visitante saved = service.save(v);

        assertNotNull(saved.getCrachaCodigo());
        verify(repo).save(saved);
    }

    @Test
    void registrarEntradaSetsDate() {
        Visitante v = new Visitante();
        v.setNomeCompleto("Maria");
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        Visitante saved = service.registrarEntrada(v);
        assertNotNull(saved.getDataEntrada());
        assertTrue(saved.getDataEntrada().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void registrarSaidaUpdatesExisting() {
        Visitante existing = new Visitante();
        existing.setId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        Visitante after = service.registrarSaida(1L);
        assertNotNull(after.getDataSaida());
    }
}
