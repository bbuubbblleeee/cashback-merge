package academy.cashbackmerge.model;

import academy.cashbackmerge.util.Messages;

public record Cashback (int cashback) {
    public Cashback {
        if (cashback <= 0) {
            throw new IllegalArgumentException(Messages.getString("cashback.error.min_value"));
        }
        if ((cashback & (cashback - 1)) != 0) {
            throw new IllegalArgumentException(Messages.getString("cashback.error.power_of_two"));
        }
    }
}
