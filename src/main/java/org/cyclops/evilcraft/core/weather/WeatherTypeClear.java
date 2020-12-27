package org.cyclops.evilcraft.core.weather;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

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
    public boolean isActive(World world) {
        return !(RAIN.isActive(world) || LIGHTNING.isActive(world));
    }

    @Override
    public void activate(ServerWorld world) {
        if (LIGHTNING.isActive(world))
            LIGHTNING.deactivate(world);
        
        if (RAIN.isActive(world))
            RAIN.deactivate(world);
    }

    @Override
    public void deactivate(ServerWorld world) {
        RAIN.activate(world);
    }

}
