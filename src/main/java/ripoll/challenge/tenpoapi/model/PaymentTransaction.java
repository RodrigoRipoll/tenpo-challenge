package ripoll.challenge.tenpoapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.NotNull;

public record PaymentTransaction(
        @NotNull
        @PositiveOrZero
        @JsonProperty("first_tx_amount")
        Double firstTxAmount,

        @NotNull
        @PositiveOrZero
        @JsonProperty("second_tx_amount")
        Double secondTxAmount) {

    public Double getTotalPayments() {
        return firstTxAmount + secondTxAmount;
    }
}
