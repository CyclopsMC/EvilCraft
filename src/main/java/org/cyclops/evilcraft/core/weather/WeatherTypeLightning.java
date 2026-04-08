package org.cyclops.evilcraft.core.weather;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.WeatherData;

/**
 * Lightning weather type.
 * @author rubensworks
 *
 */
public class WeatherTypeLightning extends WeatherType {

    public WeatherTypeLightning() {
        super("lightning");
    }

    @Override
    public boolean isActive(Level world) {
        return world.isThundering();
    }

    @Override
    public void activate(ServerLevel world) {
        activateThunder(world);
    }

    @Override
    public void deactivate(ServerLevel world) {
        world.getWeatherData().setThundering(false);
    }

    public static void activateThunder(ServerLevel world) {
        WeatherData weatherData = world.getWeatherData();
        int i = (300 + world.getRandom().nextInt(600)) * 20;
        weatherData.setRainTime(i);
        weatherData.setThunderTime(i);
        weatherData.setRaining(true);
        weatherData.setThundering(true);
    }

}
