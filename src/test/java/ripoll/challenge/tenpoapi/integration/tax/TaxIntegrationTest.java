package ripoll.challenge.tenpoapi.integration.tax;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ripoll.challenge.tenpoapi.exception.CannotRetrieveTaxException;
import ripoll.challenge.tenpoapi.integration.tax.response.TaxPercentage;
import ripoll.challenge.tenpoapi.model.TaxCacheEntry;
import ripoll.challenge.tenpoapi.repository.memoryCache.MemoryCache;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static ripoll.challenge.tenpoapi.integration.tax.TaxIntegration.TAX_CACHE_KEY;

public class TaxIntegrationTest {

  @Mock
  private TaxRestClient taxRestClient;
  @Mock
  private MemoryCache<TaxCacheEntry> memoryCache;
  private TaxIntegration taxIntegration;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    taxIntegration = new TaxIntegration(taxRestClient, memoryCache);
  }

  @Test
  void givenCurrentTaxInCache_whenGetCurrentTaxAsPercentage_thenReturnsCachedValue() {
    // Arrange
    TaxCacheEntry taxCacheEntry = new TaxCacheEntry(0.2, Duration.ofMinutes(10));
    when(memoryCache.get(TAX_CACHE_KEY)).thenReturn(taxCacheEntry);

    // Act
    double currentTax = taxIntegration.getCurrentTaxAsPercentage();

    // Assert
    assertEquals(0.2, currentTax);
  }

  @Test
  void givenNoCurrentTaxInCache_whenGetCurrentTaxAsPercentage_thenReturnsValueFromServer() {
    // Arrange
    when(taxRestClient.getCurrentTaxPercentage())
        .thenReturn(new ResponseEntity<>(new TaxPercentage(0.3), HttpStatus.OK));

    // Act
    double currentTax = taxIntegration.getCurrentTaxAsPercentage();

    // Assert
    assertEquals(0.3, currentTax);
  }

  @Test
  void givenFallbackTaxInCacheAndServerFails_whenGetCurrentTaxAsPercentage_thenReturnsFallbackTax() {
    // Arrange
    TaxCacheEntry fallbackTaxCacheEntry = new TaxCacheEntry(0.1, Duration.ofMinutes(10));
    when(memoryCache.get(TAX_CACHE_KEY)).thenReturn(fallbackTaxCacheEntry);

    when(taxRestClient.getCurrentTaxPercentage())
        .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

    // Act
    double currentTax = taxIntegration.getCurrentTaxAsPercentage();

    // Assert
    assertEquals(0.1, currentTax);
  }

  @Test
  void givenNoCurrentTaxInCacheAndServerFails_whenGetCurrentTaxAsPercentage_thenThrowsException() {
    // Arrange
    when(taxRestClient.getCurrentTaxPercentage())
        .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

    // Act & Assert
    assertThrows(CannotRetrieveTaxException.class, () -> taxIntegration.getCurrentTaxAsPercentage());
  }
}
