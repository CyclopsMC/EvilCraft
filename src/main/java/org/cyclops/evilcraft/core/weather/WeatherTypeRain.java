package org.cyclops.evilcraft.core.weather;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

/**
 * Rain weather type.
 * @author rubensworks
 *
 */
public class WeatherTypeRain extends WeatherType {

    public WeatherTypeRain() {
        super("rain");
    }

    @Override
    public boolean isActive(Level world) {
        return world.isRaining() && !world.isThundering();
    }

    @Override
    public void activate(ServerLevel world) {
        world.getLevelData().setRaining(true);
    }

    @Override
    public void deactivate(ServerLevel world) {
        world.getLevelData().setRaining(false);
    }

}
