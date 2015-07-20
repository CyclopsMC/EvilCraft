package org.cyclops.evilcraft.core.weather;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

/**
 * Lightning weather type.
 * @author rubensworks
 *
 */
public class WeatherTypeLightning extends WeatherType {

    @Override
    public boolean isActive(World world) {
        return world.isThundering();
    }

    @Override
    public void activate(World world) {
        activateThunder(world);
    }

    @Override
    public void deactivate(World world) {
        world.getWorldInfo().setThundering(false);
    }

    public static void activateThunder(World world) {
        WorldInfo worldInfo = world.getWorldInfo();
        int i = (300 + world.rand.nextInt(600)) * 20;
        worldInfo.setRainTime(i);
        worldInfo.setThunderTime(i);
        worldInfo.setRaining(true);
        worldInfo.setThundering(true);
    }

}
