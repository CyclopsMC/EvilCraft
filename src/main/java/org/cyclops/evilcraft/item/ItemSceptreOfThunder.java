package org.cyclops.evilcraft.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.weather.WeatherTypeLightning;
import org.cyclops.evilcraft.entity.item.EntityWeatherContainer;

import net.minecraft.item.Item.Properties;

/**
 * A sceptre that can spawn thunderstorms.
 * @author rubensworks
 *
 */
public class ItemSceptreOfThunder extends Item {

    public ItemSceptreOfThunder(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!world.isClientSide()) {
            WeatherTypeLightning.activateThunder((ServerWorld) world);
        }
        EntityWeatherContainer.playImpactSounds(world);
        if (!player.isCreative()) {
            itemStack.shrink(1);
        }
        return MinecraftHelpers.successAction(itemStack);
    }

}
