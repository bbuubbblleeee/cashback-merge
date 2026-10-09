package academy.cashbackmerge;

import academy.cashbackmerge.config.ConfigLoader;
import academy.cashbackmerge.exception.IllegalConfigException;
import academy.cashbackmerge.config.model.CampaignConfig;
import academy.cashbackmerge.game.GameHandler;
import academy.cashbackmerge.game.model.GameSession;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Точка входа в Cashback Merge. */
public final class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final long SEED = 10;

    private Main() {}

    public static void main(String[] args) {
        LOG.debug("Аргументы запуска: {}", String.join(" ", args));
        System.out.println("Cashback merge");
        try {
            CampaignConfig config = ConfigLoader.loadConfigFromJsonFile(Path.of("config", "campaign.example.json"));
            GameHandler gameHandler = new GameHandler(config);
            GameSession session = gameHandler.startGame(SEED);
            System.out.println(session.board().getBoardString());
        } catch (IllegalArgumentException | IllegalConfigException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
