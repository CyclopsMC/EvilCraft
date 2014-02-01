package evilcraft.api.weather;

import net.minecraft.world.World;

public class WeatherTypeLightning extends WeatherType {

    @Override
    public boolean isActive(World world) {
        return world.isThundering();
    }

    @Override
    public void activate(World world) {
        if (!RAIN.isActive(world)) 
            RAIN.activate(world);
        
        world.getWorldInfo().setThundering(true);
    }

    @Override
    public void deactivate(World world) {
        world.getWorldInfo().setThundering(false);
    }

}
