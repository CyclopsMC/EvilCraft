package org.cyclops.evilcraft.core.weather;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

/**
 * Special class that represents any weather type.
 * @author immortaleeb
 */
public class WeatherTypeAny extends WeatherType {
    public WeatherTypeAny() {
        super("any");
    }

    @Override
    public boolean isActive(Level world) {
        return true;
    }

    @Override
    public void activate(ServerLevel world) {
        // No implementation
    }

    @Override
    public void deactivate(ServerLevel world) {
        // No implementation
    }
}
