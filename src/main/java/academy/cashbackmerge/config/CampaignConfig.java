package academy.cashbackmerge.config;

import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.Reward;
import academy.cashbackmerge.model.SpawnRule;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public record CampaignConfig(
        int boardSize,
        int movesLimit,
        List<SpawnRule> spawnRules,
        List<Reward> rewardTable,
        Cashback maxRewardCashback,
        BigDecimal maxRewardAllowedRate) {}
