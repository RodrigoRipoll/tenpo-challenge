package ripoll.challenge.tenpoapi.exception;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;

public class CannotRetrieveTaxException extends RuntimeException {

    public CannotRetrieveTaxException() {
        super(MessageFormat.format("could not get the tax at {0}.", Timestamp.valueOf(LocalDateTime.now()).toString()));
    }
}
