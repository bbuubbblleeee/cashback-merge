package academy.cashbackmerge.model;

import java.math.BigDecimal;

public record SpawnRule(Cashback cashback, BigDecimal probability) {
    public static final int MAX_SCALE = 3;
}
