package ripoll.challenge.tenpoapi.model;

    public record PaymentBrief(
        PaymentTransaction payments,
        Double taxes,
        Double totalAmount
) {
}
