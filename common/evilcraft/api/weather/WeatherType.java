package evilcraft.api.weather;

import net.minecraft.world.World;

public abstract class WeatherType {
    
    public static final WeatherType CLEAR = new WeatherTypeClear();
    public static final WeatherType RAIN = new WeatherTypeRain();
    public static final WeatherType LIGHTNING = new WeatherTypeLightning();
    
    public abstract boolean isActive(World world);
    
    public abstract void activate(World world);
    public abstract void deactivate(World world);
    
    public void activate(World world, boolean activate) {
        if (activate)
            activate(world);
        else
            deactivate(world);
    }
}
