package academy.cashbackmerge.config;

import academy.cashbackmerge.config.exception.IllegalConfigException;
import academy.cashbackmerge.config.model.CampaignConfig;
import academy.cashbackmerge.config.validator.ConfigValidator;
import academy.cashbackmerge.util.Messages;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public final class ConfigLoader {
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.enable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
    }

    private ConfigLoader() {}

    public static CampaignConfig loadConfigFromJsonFile(Path path) {
        try {
            CampaignConfig config = MAPPER.readValue(path.toFile(), CampaignConfig.class);
            ConfigValidator.validate(config);
            return config;
        } catch (FileNotFoundException e) {
            throw new IllegalConfigException(Messages.getString("config.error.not_found"), e);
        } catch (JacksonException e) {
            throw new IllegalConfigException(buildJacksonErrorMessage(e), e);
        } catch (IOException e) {
            throw new IllegalConfigException(Messages.getString("config.error.io_exception"), e);
        }
    }

    public static CampaignConfig loadConfigFromJsonString(String json) {
        try {
            CampaignConfig config = MAPPER.readValue(json, CampaignConfig.class);
            ConfigValidator.validate(config);
            return config;
        } catch (JacksonException e) {
            throw new IllegalConfigException(buildJacksonErrorMessage(e), e);
        }
    }

    private static String buildJacksonErrorMessage(JacksonException e) {
        Throwable cause = e.getCause();
        String message = Messages.getString("config.error");
        if (cause != null) {
            message += ": " + cause.getMessage();
        }
        return message;
    }
}
