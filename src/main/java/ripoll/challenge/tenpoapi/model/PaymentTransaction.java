package ripoll.challenge.tenpoapi.model;

public record PaymentTransaction(Double firstTxAmount, Double secondTxAmount) {

    public Double getTotalPayments() {
        return firstTxAmount + secondTxAmount;
    }
}
