package org.cyclops.evilcraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Bottle that is retrieved when right-clicking a poison source.
 * @author rubensworks
 *
 */
public class ItemPoisonBottle extends Item {

    public ItemPoisonBottle(Item.Properties properties) {
        super(properties);
        NeoForge.EVENT_BUS.addListener(this::onPoisonRightClick);
    }

    public void onPoisonRightClick(PlayerInteractEvent.RightClickBlock event) {
        InteractionHand hand = event.getEntity().getUsedItemHand();
        // Return poison bottle instead of water bottle when right clicking poison fluid source with empty bottle.
        if(hand != null && !event.getEntity().getItemInHand(hand).isEmpty() &&
                event.getEntity().getItemInHand(hand).getItem() == Items.GLASS_BOTTLE) {
            HitResult pos = this.getPlayerPOVHitResult(event.getLevel(), event.getEntity(), ClipContext.Fluid.SOURCE_ONLY);
            if(pos != null && pos.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = BlockPos.containing(pos.getLocation());
                if(event.getLevel().mayInteract(event.getEntity(), blockPos) &&
                        event.getEntity().mayUseItemAt(blockPos, event.getFace(), event.getEntity().getItemInHand(hand)) &&
                        event.getLevel().getBlockState(blockPos).getFluidState().is(Fluids.WATER)) {
                    if(event.getLevel().getFluidState(blockPos).getType() == RegistryEntries.FLUID_POISON.get()) {
                        InventoryHelpers.tryReAddToStack(event.getEntity(), event.getEntity().getItemInHand(hand), new ItemStack(this), hand);
                        event.getLevel().removeBlock(blockPos, false);
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemColor implements net.minecraft.client.color.item.ItemColor {
        @Override
        public int getColor(ItemStack itemStack, int renderPass) {
            return renderPass == 0 ? Helpers.RGBAToInt(77, 117, 15, 255) : -1;
        }
    }

}
