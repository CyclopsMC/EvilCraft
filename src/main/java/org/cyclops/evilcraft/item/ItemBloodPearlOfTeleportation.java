package org.cyclops.evilcraft.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;
import org.cyclops.evilcraft.entity.item.EntityBloodPearl;

/**
 * Ender pearl that runs on blood.
 * @author rubensworks
 *
 */
public class ItemBloodPearlOfTeleportation extends ItemBloodContainer {

    public ItemBloodPearlOfTeleportation(Item.Properties properties) {
        super(properties, 1000);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(canConsume(100, itemStack, player)) {
            this.consume(100, itemStack, player);
            world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));

            if (!world.isClientSide()) {
                EntityBloodPearl pearl = new EntityBloodPearl(world, player);
                // MCP: shoot
                pearl.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.0F, 0.0F);
                pearl.setDeltaMovement(pearl.getDeltaMovement().multiply(3, 3, 3));
                world.addFreshEntity(pearl);
            }

            return MinecraftHelpers.successAction(itemStack);
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.PASS, itemStack);
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        return Rarity.UNCOMMON;
    }

}
