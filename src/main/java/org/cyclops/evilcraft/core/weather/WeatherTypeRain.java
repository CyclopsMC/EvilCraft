package org.cyclops.evilcraft.core.weather;

import net.minecraft.world.World;

/**
 * Rain weather type.
 * @author rubensworks
 *
 */
public class WeatherTypeRain extends WeatherType {

    @Override
    public boolean isActive(World world) {
        return world.isRaining() && !world.isThundering();
    }

    @Override
    public void activate(World world) {
        world.getWorldInfo().setRaining(true);
    }

    @Override
    public void deactivate(World world) {
        world.getWorldInfo().setRaining(false);
    }

}
