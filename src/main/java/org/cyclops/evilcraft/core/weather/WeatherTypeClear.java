package org.cyclops.evilcraft.core.weather;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

/**
 * Clear weather type.
 * @author rubensworks
 *
 */
public class WeatherTypeClear extends WeatherType {
    public WeatherTypeClear() {
        super("clear");
    }

    @Override
    public boolean isActive(Level world) {
        return !(RAIN.isActive(world) || LIGHTNING.isActive(world));
    }

    @Override
    public void activate(ServerLevel world) {
        if (LIGHTNING.isActive(world))
            LIGHTNING.deactivate(world);

        if (RAIN.isActive(world))
            RAIN.deactivate(world);
    }

    @Override
    public void deactivate(ServerLevel world) {
        RAIN.activate(world);
    }

}
