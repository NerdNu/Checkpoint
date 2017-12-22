package nu.nerd.checkpoint;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static String formatLocation(Location location) {
        return String.format("(%.1f, %.1f, %.1f, %s)",
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getWorld().getName());
    }

    public static String getString(Map config, String key) {
        Object value = config.get(key);
        if (value == null || !(value instanceof String)) {
            return null;
        }
        return (String) value;
    }

    public static boolean getBoolean(Map config, String key, boolean defaultValue) {
        Object value = config.get(key);
        if (value == null || !(value instanceof Boolean)) {
            return defaultValue;
        }
        return (boolean) value;
    }

    public static Map<String, Object> getSection(Map config, String key) {
        Object value = config.get(key);
        if (value == null || !(value instanceof Map)) {
            return null;
        }
        return (Map<String, Object>) value;
    }

}
