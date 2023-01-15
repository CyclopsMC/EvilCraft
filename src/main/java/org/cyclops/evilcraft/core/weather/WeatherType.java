package org.cyclops.evilcraft.core.weather;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.lang.reflect.Field;

/**
 * Abstract class for weather types.
 * @author rubensworks
 *
 */
public abstract class WeatherType {

    /**
     * Any weather type.
     */
    public static final WeatherType ANY = new WeatherTypeAny();
    /**
     * Clear weather.
     */
    public static final WeatherType CLEAR = new WeatherTypeClear();
    /**
     * Rain.
     */
    public static final WeatherType RAIN = new WeatherTypeRain();
    /**
     * Lightning.
     */
    public static final WeatherType LIGHTNING = new WeatherTypeLightning();

    /**
     * Array that contains all possible weather types
     */
    public static final WeatherType[] WEATHER_TYPES = {CLEAR, RAIN, LIGHTNING};

    private final String name;

    protected WeatherType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Check if this weather is active in the given world.
     * @param world The world.
     * @return If it is active.
     */
    public abstract boolean isActive(Level world);

    /**
     * Activate this weather in the given world.
     * @param world The world.
     */
    public abstract void activate(ServerLevel world);
    /**
     * Deactivate this weather in the given world.
     * @param world The world.
     */
    public abstract void deactivate(ServerLevel world);

    /**
     * Activate or deactivate this weather in the given world.
     * @param world The world.
     * @param activate If the weather must be activated.
     */
    public void activate(ServerLevel world, boolean activate) {
        if (activate)
            activate(world);
        else
            deactivate(world);
    }

    /**
     * Returns the current active weather type.
     *
     * @param world The world.
     * @return The current active {@link WeatherType},
     *         or null in case no known active weather
     *         type was found.
     */
    public static WeatherType getActiveWeather(Level world) {
        for (WeatherType type : WEATHER_TYPES) {
            if (type.isActive(world))
                return type;
        }

        return null;
    }

    /**
     * Get a weather type by name.
     * @param type The type name.
     * @return The weather type.
     */
    public static WeatherType valueOf(String type) {
        Field field;
        try {
            field = WeatherType.class.getField(type);
        } catch (NoSuchFieldException e) {
            return null;
        } catch (SecurityException e) {
            return null;
        }
        try {
            return (WeatherType) field.get(null);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }
}
