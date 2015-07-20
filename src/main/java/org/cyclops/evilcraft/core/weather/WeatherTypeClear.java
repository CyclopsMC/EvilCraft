package org.cyclops.evilcraft.core.weather;

import net.minecraft.world.World;

/**
 * Clear weather type.
 * @author rubensworks
 *
 */
public class WeatherTypeClear extends WeatherType {
    @Override
    public boolean isActive(World world) {
        return !(RAIN.isActive(world) || LIGHTNING.isActive(world));
    }

    @Override
    public void activate(World world) {
        if (LIGHTNING.isActive(world))
            LIGHTNING.deactivate(world);
        
        if (RAIN.isActive(world))
            RAIN.deactivate(world);
    }

    @Override
    public void deactivate(World world) {
        RAIN.activate(world);
    }

}
