package academy.cashbackmerge.util;

import java.util.ResourceBundle;

public final class Messages {
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("messages");

    private Messages() {}

    public static String getString(String key) {
        return RESOURCE_BUNDLE.getString(key);
    }
}
