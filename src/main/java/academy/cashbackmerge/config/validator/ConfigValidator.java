package academy.cashbackmerge.config.validator;

import academy.cashbackmerge.config.exception.IllegalConfigException;
import academy.cashbackmerge.config.model.CampaignConfig;
import academy.cashbackmerge.model.Cashback;
import academy.cashbackmerge.model.Reward;
import academy.cashbackmerge.model.SpawnRule;
import academy.cashbackmerge.util.Messages;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ConfigValidator {
    private static final int MAX_SCALE = SpawnRule.MAX_SCALE;

    private ConfigValidator() {}

    public static void validate(CampaignConfig campaignConfig) {
        validateBoardSize(campaignConfig.boardSize());
        validateMovesLimit(campaignConfig.movesLimit());
        validateSpawnRules(campaignConfig.spawnRules());
        validateRewardTable(campaignConfig.rewardTable());
        validateMaxRewardAllowedRate(campaignConfig.maxRewardAllowedRate());
    }

    private static void validateBoardSize(int boardSize) {
        assertGreaterThan(boardSize, 1, Messages.getString("board_size.error.min_value"));
    }

    private static void validateMovesLimit(int movesLimit) {
        assertGreaterThan(movesLimit, 0, Messages.getString("moves_limit.error.min_value"));
    }

    private static void validateSpawnRules(List<SpawnRule> spawnRules) {
        assertNotNull(spawnRules, Messages.getString("spawn_rules.error.empty"));
        assertListNotEmpty(spawnRules, Messages.getString("spawn_rules.error.empty"));

        BigDecimal sumOfProbabilities = BigDecimal.ZERO;
        Set<Cashback> cashbacks = new HashSet<>();
        for (SpawnRule rule : spawnRules) {
            assertNotNull(rule.cashback(), Messages.getString("cashback.error.null"));
            validateProbability(rule.probability());
            if (cashbacks.contains(rule.cashback())) {
                throw new IllegalConfigException(Messages.getString("spawn_rules.cashback.error.already_exists"));
            }
            cashbacks.add(rule.cashback());
            sumOfProbabilities = sumOfProbabilities.add(rule.probability());
        }
        if (sumOfProbabilities.compareTo(BigDecimal.ONE) != 0) {
            throw new IllegalConfigException(Messages.getString("probability.error.sum"));
        }
    }

    private static void validateRewardTable(List<Reward> rewardTable) {
        assertNotNull(rewardTable, Messages.getString("reward_table.error.empty"));
        assertListNotEmpty(rewardTable, Messages.getString("reward_table.error.empty"));
        Set<Cashback> cashbacks = new HashSet<>();
        for (Reward reward : rewardTable) {
            assertNotNull(reward.cashback(), Messages.getString("cashback.error.null"));
            if (reward.reward() < 0) {
                throw new IllegalConfigException(Messages.getString("reward_table.error.min_value"));
            }
            if (cashbacks.contains(reward.cashback())) {
                throw new IllegalConfigException(Messages.getString("reward_table.cashback.error.already_exists"));
            }
            cashbacks.add(reward.cashback());
        }
    }

    private static void validateMaxRewardAllowedRate(BigDecimal maxRewardAllowedRate) {
        assertNotNull(maxRewardAllowedRate, Messages.getString("max_reward_allowed_rate.error.null"));
        assertBigDecimalPositive(maxRewardAllowedRate, Messages.getString("max_reward_allowed_rate.error.min_value"));
        assertBigDecimalScale(
                maxRewardAllowedRate, MAX_SCALE, Messages.getString("max_reward_allowed_rate.error.max_scale"));
    }

    private static void validateProbability(BigDecimal probability) {
        assertNotNull(probability, Messages.getString("probability.error.null"));
        assertBigDecimalPositive(probability, Messages.getString("probability.error.min_value"));
        assertBigDecimalScale(probability, MAX_SCALE, Messages.getString("probability.error.max_scale"));
    }

    private static void assertGreaterThan(int valueToCheck, int limit, String message) {
        if (valueToCheck <= limit) {
            throw new IllegalConfigException(message);
        }
    }

    private static void assertNotNull(Object o, String message) {
        if (o == null) {
            throw new IllegalConfigException(message);
        }
    }

    private static void assertListNotEmpty(List<?> list, String message) {
        if (list.isEmpty()) {
            throw new IllegalConfigException(message);
        }
    }

    private static void assertBigDecimalPositive(BigDecimal value, String message) {
        if (value.signum() <= 0) {
            throw new IllegalConfigException(message);
        }
    }

    private static void assertBigDecimalScale(BigDecimal value, int scale, String message) {
        if (value.stripTrailingZeros().scale() > scale) {
            throw new IllegalConfigException(message);
        }
    }
}
