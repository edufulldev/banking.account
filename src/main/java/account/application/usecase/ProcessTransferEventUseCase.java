package account.application.usecase;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.postgresql.util.PSQLException;
import java.util.UUID;

@Service
public class ProcessTransferEventUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessTransferEventUseCase.class);
    private final ProcessedEventRegistrationService registrationService;

    public ProcessTransferEventUseCase(ProcessedEventRegistrationService registrationService) {
        this.registrationService = registrationService;

    }

    public void execute(UUID eventId) {

       try {

           registrationService.register(eventId);
           log.info("Evento registrado como processado: {}", eventId);

       } catch (DataIntegrityViolationException ex) {

           Throwable cause = ex;

           while (cause != null) {

               if (cause instanceof PSQLException pgException
                       && "23505".equals(pgException.getSQLState())
                       && pgException.getServerErrorMessage() != null
                       && "processed_events_pkey".equals(
                       pgException.getServerErrorMessage().getConstraint())) {

                   log.info("Evento duplicado ignorado: {}", eventId);
                   return;
               }
               cause = cause.getCause();
           }
           throw ex;
       }
    }

}
