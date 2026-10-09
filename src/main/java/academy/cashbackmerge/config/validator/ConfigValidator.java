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
    private static final int MAX_SCALE = 3;

    private ConfigValidator() {}

    public static void validate(CampaignConfig campaignConfig) {
        validateBoardSize(campaignConfig.boardSize());
        validateMovesLimit(campaignConfig.movesLimit());
        validateSpawnRules(campaignConfig.spawnRules());
        validateRewardTable(campaignConfig.rewardTable());
        validateMaxRewardAllowedRate(campaignConfig.maxRewardAllowedRate());
    }

    private static void validateBoardSize(int boardSize) {
        if (boardSize <= 1) {
            throw new IllegalConfigException(Messages.getString("board_size.error.min_value"));
        }
    }

    private static void validateMovesLimit(int movesLimit) {
        if (movesLimit <= 0) {
            throw new IllegalConfigException(Messages.getString("moves_limit.error.min_value"));
        }
    }

    private static void validateSpawnRules(List<SpawnRule> spawnRules) {
        if (spawnRules == null || spawnRules.isEmpty()) {
            throw new IllegalConfigException(Messages.getString("spawn_rules.error.empty"));
        }
        BigDecimal sumOfProbabilities = BigDecimal.ZERO;
        Set<Cashback> cashbacks = new HashSet<>();
        for (SpawnRule rule : spawnRules) {
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
        if (rewardTable == null || rewardTable.isEmpty()) {
            throw new IllegalConfigException(Messages.getString("reward_table.error.empty"));
        }
        Set<Cashback> cashbacks = new HashSet<>();
        for (Reward reward : rewardTable) {
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
        if (maxRewardAllowedRate == null) {
            throw new IllegalConfigException(Messages.getString("max_reward_allowed_rate.error.null"));
        }
        if (maxRewardAllowedRate.signum() < 0) {
            throw new IllegalConfigException(Messages.getString("max_reward_allowed_rate.error.min_value"));
        }
    }

    private static void validateProbability(BigDecimal probability) {
        if (probability == null) {
            throw new IllegalConfigException(Messages.getString("probability.error.null"));
        }
        if (probability.signum() <= 0) {
            throw new IllegalConfigException(Messages.getString("probability.error.min_value"));
        }
        if (probability.scale() >= MAX_SCALE) {
            throw new IllegalConfigException(Messages.getString("probability.error.max_scale"));
        }
    }
}
