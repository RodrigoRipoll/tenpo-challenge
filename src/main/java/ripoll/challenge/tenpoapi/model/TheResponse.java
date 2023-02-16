package ripoll.challenge.tenpoapi.model;

public record TheResponse(
        PaymentTransaction payments,
        Double taxes,
        Double amount
) {
}
