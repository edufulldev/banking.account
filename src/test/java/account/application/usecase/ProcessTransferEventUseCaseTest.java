package account.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class ProcessTransferEventUseCaseTest {

    private ProcessedEventRegistrationService registrationService;
    private ProcessTransferEventUseCase useCase;

    @BeforeEach
    void setUp() {
        registrationService = mock(ProcessedEventRegistrationService.class);
        useCase = new ProcessTransferEventUseCase(registrationService);
    }

    @Test
    void shouldRegisterNewEvent() {

        UUID eventId = UUID.randomUUID();
        useCase.execute(eventId);
        verify(registrationService, times(1)).register(eventId);
    }

    @Test
    void shouldIgnoreAlreadyProcessedEvent() {

        UUID eventId = UUID.randomUUID();

        ServerErrorMessage serverError = new ServerErrorMessage(
                "SERROR\0" + "C23505\0" + "nprocessed_events_pkey\0" + "\0");

        PSQLException pgException = new PSQLException(serverError);

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("Evento duplicado", pgException);

        doThrow(exception).when(registrationService).register(eventId);
        assertDoesNotThrow(() -> useCase.execute(eventId));
        verify(registrationService, times(1)).register(eventId);
    }

    @Test
    void shouldPropagateOtherIntegrityViolations() {

        UUID eventId = UUID.randomUUID();

        SQLException sqlException = new SQLException("Violação de chave estrangeira", "23503");

        DataIntegrityViolationException exception =
                new DataIntegrityViolationException("Erro de integridade", sqlException);

        doThrow(exception).when(registrationService).register(eventId);

        assertThrows(DataIntegrityViolationException.class, () -> useCase.execute(eventId));

        verify(registrationService, times(1)).register(eventId);
    }

}