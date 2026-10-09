package academy.cashbackmerge.support;

import academy.cashbackmerge.config.model.CampaignConfig;
import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.Reward;
import academy.cashbackmerge.model.SpawnRule;
import java.math.BigDecimal;
import java.util.List;

public final class TestConfig {
    private static final CampaignConfig CONFIG;

    private TestConfig() {}

    static {
        List<SpawnRule> spawnRules = List.of(
                new SpawnRule(new Cashback(1), new BigDecimal("0.15")),
                new SpawnRule(new Cashback(2), new BigDecimal("0.85")));
        List<Reward> rewardTable = List.of(new Reward(new Cashback(1), 0), new Reward(new Cashback(2), 1000));
        CONFIG = CampaignConfig.builder()
                .boardSize(4)
                .movesLimit(10)
                .spawnRules(spawnRules)
                .rewardTable(rewardTable)
                .maxRewardCashback(new Cashback(32))
                .maxRewardAllowedRate(new BigDecimal("0.20"))
                .build();
    }

    public static CampaignConfig getConfig() {
        return CONFIG;
    }
}
