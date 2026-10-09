package academy.cashbackmerge.config;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import academy.cashbackmerge.config.exception.IllegalConfigException;
import academy.cashbackmerge.util.Messages;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Парсинг конфига кампании")
public class ConfigLoaderTest {

    @Test
    @DisplayName("Валидный конфиг корректно парсится в объект")
    void validConfigIsAccepted() {
        assertThatCode(() -> ConfigLoader.loadConfigFromJsonFile(Path.of("config", "campaign.example.json")))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Случай с несуществующим конфигом корректно обрабатывается")
    void validConfigNotFound() {
        assertThatThrownBy(() -> ConfigLoader.loadConfigFromJsonFile(Path.of("config", "not.found.config")))
                .isInstanceOf(IllegalConfigException.class)
                .hasMessage(Messages.getString("config.error.not_found"));
    }
}
