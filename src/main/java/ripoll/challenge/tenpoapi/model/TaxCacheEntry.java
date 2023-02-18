package ripoll.challenge.tenpoapi.model;

import java.time.Duration;
import java.time.Instant;

public record TaxCacheEntry(
        Long createAt,
        Long expirationAt,
        Double tax
) {
    public TaxCacheEntry(Double tax, Duration validWindow) {
        this(Instant.now().toEpochMilli(), Instant.now().plusMillis(validWindow.toMillis()).toEpochMilli(), tax);
    }

    public boolean isExpired() {
        return Instant.now().toEpochMilli() >= expirationAt;
    }
}
