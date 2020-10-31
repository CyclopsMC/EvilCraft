package org.cyclops.evilcraft.core.helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Helpers for items.
 * @author rubensworks
 *
 */
public class ItemHelpers {

	private static final int MB_FILL_PERTICK = GeneralConfig.mbFlowRate;
    private static ItemStack bloodBucket = null;
	
	/**
     * Check if the given item is activated.
     * @param itemStack The item to check
     * @return If it is an active container.
     */
    public static boolean isActivated(ItemStack itemStack) {
        return !itemStack.isEmpty() && itemStack.hasTag() && itemStack.getTag().getBoolean("enabled");
    }
    
    /**
     * Toggle activation for the given item.
     * @param itemStack The item to toggle.
     */
    public static void toggleActivation(ItemStack itemStack) {
        CompoundNBT tag = itemStack.getTag();
        if(tag == null) {
            tag = new CompoundNBT();
            itemStack.setTag(tag);
        }
        tag.putBoolean("enabled", !isActivated(itemStack));
    }
    
    /**
     * Get the integer value of the given ItemStack.
     * @param itemStack The item to check.
     * @param tag The tag in NBT for storing this value.
     * @return The integer value for the given tag.
     */
    public static int getNBTInt(ItemStack itemStack, String tag) {
        if(itemStack.isEmpty() || itemStack.getTag() == null) {
            return 0;
        }
        return itemStack.getTag().getInt(tag);
    }
    
    /**
     * Set the integer value of the given ItemStack for the given tag.
     * @param itemStack The item to change.
     * @param integer The new integer value.
     * @param tag The tag in NBT for storing this value.
     */
    public static void setNBTInt(ItemStack itemStack, int integer, String tag) {
        CompoundNBT tagCompound = itemStack.getTag();
        if(tagCompound == null) {
            tagCompound = new CompoundNBT();
            itemStack.setTag(tagCompound);
        }
        tagCompound.putInt(tag, integer);
    }
    
    /**
     * Run an auto-fill tick for filling currently held container items from this item.
     * @param toDrain The item handler to drain from.
     * @param world The world.
     * @param entity The entity that holds this item.
     * @param fillBuckets If buckets should be filled.
     */
    public static void updateAutoFill(IFluidHandlerItem toDrain, World world, Entity entity, boolean fillBuckets) {
    	if(entity instanceof PlayerEntity && !world.isRemote()) {
            FluidStack tickFluid = toDrain.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            if(!tickFluid.isEmpty()) {
                PlayerEntity player = (PlayerEntity) entity;
                for (Hand hand : Hand.values()) {
                    ItemStack held = player.getHeldItem(hand);
                    if (!held.isEmpty() && (fillBuckets || held.getItem() != Items.BUCKET)) {
                        ItemStack toFill = held.split(1);
                        ItemStack filled = tryFillContainerForPlayer(toDrain, toFill, tickFluid, player);
                        if (!filled.isEmpty()) {
                            if (player.getHeldItem(hand).isEmpty()) {
                                player.setHeldItem(hand, filled);
                            } else {
                                player.addItemStackToInventory(filled);
                            }
                        } else {
                            held.grow(1);
                        }
                    }
                }
            }
        }
    }

    /**
     * Tries to fill a container item in a player inventory.
     * @param toDrain The item handler to drain from.
     * @param toFill The container to try to fill.
     * @param tickFluid The fluid to fill with.
     * @param player The player that is the owner of toFill.
     * @return The filled container
     */
    public static ItemStack tryFillContainerForPlayer(IFluidHandlerItem toDrain, ItemStack toFill, FluidStack tickFluid, PlayerEntity player) {
        int maxFill = MB_FILL_PERTICK;
        if (toFill.getItem() == Items.BUCKET) {
            maxFill = FluidHelpers.BUCKET_VOLUME;
        }
        if(!toFill.isEmpty() && toFill != toDrain.getContainer() && FluidUtil.getFluidHandler(toFill) != null
                && player.getItemInUseCount() == 0 && FluidUtil.tryFillContainer(toFill, toDrain, Math.min(maxFill, tickFluid.getAmount()), player, false).isSuccess()) {
            return FluidUtil.tryFillContainer(toFill, toDrain, Math.min(maxFill, tickFluid.getAmount()), player, true).getResult();
        }
        return ItemStack.EMPTY;
    }

    /**
     * @return The filled blood bucket.
     */
    public static ItemStack getBloodBucket() {
        if (bloodBucket == null) {
            bloodBucket = new ItemStack(RegistryEntries.ITEM_BUCKET_BLOOD);
        }
        return bloodBucket;
    }
	
}
