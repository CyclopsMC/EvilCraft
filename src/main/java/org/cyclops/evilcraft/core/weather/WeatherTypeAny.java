package org.cyclops.evilcraft.core.weather;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Special class that represents any weather type.
 * @author immortaleeb
 */
public class WeatherTypeAny extends WeatherType {
    public WeatherTypeAny() {
        super("any");
    }

    @Override
    public boolean isActive(World world) {
        return true;
    }

    @Override
    public void activate(ServerWorld world) {
        // No implementation
    }

    @Override
    public void deactivate(ServerWorld world) {
        // No implementation
    }
}
