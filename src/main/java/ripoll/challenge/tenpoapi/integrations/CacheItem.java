package ripoll.challenge.tenpoapi.integrations;

import java.time.Duration;
import java.time.Instant;


public record CacheItem(
        Long createAt,
        Long expirationAt,
        Double tax
) {
    public CacheItem(Double tax, Duration validWindow) {
        this(Instant.now().toEpochMilli(), Instant.now().toEpochMilli() + validWindow.toMillis(),tax);
    }
    public boolean isExpired() {
        return Instant.now().toEpochMilli() >= expirationAt;
    }
}
