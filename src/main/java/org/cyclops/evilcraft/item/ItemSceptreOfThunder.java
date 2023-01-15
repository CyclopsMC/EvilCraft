package org.cyclops.evilcraft.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.weather.WeatherTypeLightning;
import org.cyclops.evilcraft.entity.item.EntityWeatherContainer;

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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!world.isClientSide()) {
            WeatherTypeLightning.activateThunder((ServerLevel) world);
        }
        EntityWeatherContainer.playImpactSounds(world);
        if (!player.isCreative()) {
            itemStack.shrink(1);
        }
        return MinecraftHelpers.successAction(itemStack);
    }

}
