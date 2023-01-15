package org.cyclops.evilcraft.core.weather;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;

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
    public boolean isActive(Level world) {
        return world.isThundering();
    }

    @Override
    public void activate(ServerLevel world) {
        activateThunder(world);
    }

    @Override
    public void deactivate(ServerLevel world) {
        ((ServerLevelData) world.getLevelData()).setThundering(false);
    }

    public static void activateThunder(ServerLevel world) {
        ServerLevelData worldInfo = (ServerLevelData) world.getLevelData();
        int i = (300 + world.random.nextInt(600)) * 20;
        worldInfo.setRainTime(i);
        worldInfo.setThunderTime(i);
        worldInfo.setRaining(true);
        worldInfo.setThundering(true);
    }

}
