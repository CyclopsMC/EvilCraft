package evilcraft.api.weather;

import net.minecraft.world.World;

/**
 * Rain weather type.
 * @author rubensworks
 *
 */
public class WeatherTypeRain extends WeatherType {

    @Override
    public boolean isActive(World world) {
        return world.isRaining();
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
