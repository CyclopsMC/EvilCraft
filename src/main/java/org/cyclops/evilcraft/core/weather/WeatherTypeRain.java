package org.cyclops.evilcraft.core.weather;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

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
    public boolean isActive(World world) {
        return world.isRaining() && !world.isThundering();
    }

    @Override
    public void activate(ServerWorld world) {
        world.getWorldInfo().setRaining(true);
    }

    @Override
    public void deactivate(ServerWorld world) {
        world.getWorldInfo().setRaining(false);
    }

}
