package evilcraft.item;

import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.weather.WeatherTypeLightning;
import evilcraft.entity.item.EntityWeatherContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * A sceptre that can spawn thunderstorms.
 * @author rubensworks
 *
 */
public class SceptreOfThunder extends ConfigurableItem {

    private static SceptreOfThunder _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new SceptreOfThunder(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SceptreOfThunder getInstance() {
        return _instance;
    }

    private SceptreOfThunder(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        WeatherTypeLightning.activateThunder(world);
        EntityWeatherContainer.playImpactSounds(world);
        if(!player.capabilities.isCreativeMode) --itemStack.stackSize;
        return itemStack;
    }

}
