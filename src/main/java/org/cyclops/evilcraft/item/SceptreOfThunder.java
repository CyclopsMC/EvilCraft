package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.weather.WeatherTypeLightning;
import org.cyclops.evilcraft.entity.item.EntityWeatherContainer;

/**
 * A sceptre that can spawn thunderstorms.
 * @author rubensworks
 *
 */
public class SceptreOfThunder extends ConfigurableItem {

    private static SceptreOfThunder _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static SceptreOfThunder getInstance() {
        return _instance;
    }

    public SceptreOfThunder(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        WeatherTypeLightning.activateThunder(world);
        EntityWeatherContainer.playImpactSounds(world);
        if(!player.capabilities.isCreativeMode) --itemStack.stackSize;
        return MinecraftHelpers.successAction(itemStack);
    }

}
