package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.core.fluid.FluidContainerItemWrapperWithSimulation;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

/**
 * Specialized item for the {@link EntangledChalice} blockState.
 * @author rubensworks
 */
public class EntangledChaliceItem extends ItemBlockFluidContainer {

	/**
     * Make a new instance.
     * @param block The blockState instance.
     */
    public EntangledChaliceItem(Block block) {
        super(block);
    }

    protected void autofill(IFluidHandler source, ItemStack itemStack, World world, Entity entity) {
        if(entity instanceof EntityPlayer && !world.isRemote) {
            EntityPlayer player = (EntityPlayer) entity;
            FluidStack tickFluid;
            PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
            do {
                tickFluid = FluidHelpers.getFluid(source);
                ItemStack toFill = it.next();
                if (tickFluid != null && toFill != null && toFill.getCount() == 1) {
                    ItemStack filled = ItemHelpers.tryFillContainerForPlayer(source, itemStack, toFill, tickFluid, player);
                    if (filled != null) {
                        it.replace(filled);
                    }
                }
            } while(tickFluid != null && tickFluid.amount > 0 && it.hasNext());
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidHandler(stack, TileEntangledChalice.BASE_CAPACITY);
    }

    @Override
    protected void itemStackDataToTile(ItemStack itemStack, TileEntity tile) {
        super.itemStackDataToTile(itemStack, tile);
        // Convert fluid data
        BlockTankHelpers.itemStackDataToTile(itemStack, tile);

        // Convert tank id
        String tankId = ((TileEntangledChalice) tile).getWorldTankId();
        EntangledChaliceItem.FluidHandler fluidHandler = (EntangledChaliceItem.FluidHandler) FluidUtil.getFluidHandler(itemStack);
        fluidHandler.setTankID(tankId);
    }

    public static class FluidHandler extends FluidContainerItemWrapperWithSimulation {

        public FluidHandler(ItemStack container, int capacity) {
            super(container, capacity);
        }

        @Override
        public FluidStack getFluid() {
            return WorldSharedTankCache.getInstance().getTankContent(getTankID());
        }

        @Override
        protected void setFluid(FluidStack fluidStack) {
            WorldSharedTankCache.getInstance().setTankContent(getTankID(), fluidStack);
        }

        /**
         * Get the tank id from the container.
         * @return The tank id.
         */
        public String getTankID() {
            NBTTagCompound tag = ItemStackHelpers.getSafeTagCompound(getContainer());
            if(tag.hasKey(WorldSharedTank.NBT_TANKID)) {
                tag.setString(WorldSharedTank.NBT_TANKID, "invalid");
            }
            return tag.getString(WorldSharedTank.NBT_TANKID);
        }

        /**
         * Set the tank id for the container.
         * @param tankID The tank id.
         */
        public void setTankID(String tankID) {
            NBTTagCompound tag = ItemStackHelpers.getSafeTagCompound(getContainer());
            tag.setString(WorldSharedTank.NBT_TANKID, tankID);
        }

        /**
         * Set a new unique tank id for the container.
         */
        public void setNextTankID() {
            setTankID(Integer.toString(EvilCraft.globalCounters.getNext("EntangledChalice")));
        }
    }
}
