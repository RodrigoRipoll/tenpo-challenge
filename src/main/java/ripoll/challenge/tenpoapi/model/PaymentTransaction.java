package ripoll.challenge.tenpoapi.model;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;

public record PaymentTransaction(
        @NotNull
        @PositiveOrZero
        Double firstTxAmount,

        @NotNull
        @PositiveOrZero
        Double secondTxAmount) {

    public Double getTotalPayments() {
        return firstTxAmount + secondTxAmount;
    }
}
