package org.cyclops.evilcraft.core.weather;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerWorldInfo;

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
    public boolean isActive(World world) {
        return world.isThundering();
    }

    @Override
    public void activate(ServerWorld world) {
        activateThunder(world);
    }

    @Override
    public void deactivate(ServerWorld world) {
        ((IServerWorldInfo) world.getLevelData()).setThundering(false);
    }

    public static void activateThunder(ServerWorld world) {
        IServerWorldInfo worldInfo = (IServerWorldInfo) world.getLevelData();
        int i = (300 + world.random.nextInt(600)) * 20;
        worldInfo.setRainTime(i);
        worldInfo.setThunderTime(i);
        worldInfo.setRaining(true);
        worldInfo.setThundering(true);
    }

}
