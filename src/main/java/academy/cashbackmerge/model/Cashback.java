package academy.cashbackmerge.model;

import academy.cashbackmerge.exception.IllegalCashbackException;
import academy.cashbackmerge.util.Messages;
import com.fasterxml.jackson.annotation.JsonCreator;

public record Cashback(int cashback) {

    public Cashback {
        if (cashback <= 0) {
            throw new IllegalCashbackException(Messages.getString("cashback.error.min_value"));
        }
        validateIfPowOfTwo();
    }

    @JsonCreator
    public Cashback(String value) {
        this(Integer.parseInt(value.trim()));
    }

    @Override
    public String toString() {
        return this.cashback + "%";
    }

    private void validateIfPowOfTwo() {
        if ((cashback & (cashback - 1)) != 0) {
            throw new IllegalCashbackException(Messages.getString("cashback.error.power_of_two"));
        }
    }
}
