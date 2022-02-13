package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.block.BlockEntangledChalice;
import org.cyclops.evilcraft.core.fluid.FluidContainerItemWrapperWithSimulation;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.item.Item.Properties;

/**
 * Specialized item for the {@link BlockEntangledChalice} blockState.
 * @author rubensworks
 */
public class ItemEntangledChalice extends ItemBlockFluidContainer {

    public static String[] namePartsArray = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale phnglui mglwnafh cthulhu rlyeh wgahnagl fhtagnbaguette".split(" ");

    public ItemEntangledChalice(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }

    @Override
    protected void autofill(int itemSlot, IFluidHandlerItem source, World world, Entity entity) {
        if(entity instanceof PlayerEntity && !world.isClientSide()) {
            PlayerEntity player = (PlayerEntity) entity;
            FluidStack tickFluid;
            PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
            do {
                tickFluid = FluidHelpers.getFluid(source);
                ItemStack toFill = it.next();
                if (tickFluid != null && !toFill.isEmpty() && toFill.getCount() == 1) {
                    ItemStack filled = ItemHelpers.tryFillContainerForPlayer(source, toFill, tickFluid, player);
                    if (!filled.isEmpty()) {
                        it.replace(filled);
                        player.inventory.setItem(itemSlot, source.getContainer());
                    }
                }
            } while(tickFluid != null && tickFluid.getAmount() > 0 && it.hasNext());
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new FluidHandler(stack, TileEntangledChalice.BASE_CAPACITY);
    }

    @Override
    protected boolean itemStackDataToTile(ItemStack itemStack, TileEntity tile) {
        super.itemStackDataToTile(itemStack, tile);
        // Convert tank id
        ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(itemStack).orElse(null);
        String tankId = fluidHandler.getTankID();
        ((TileEntangledChalice) tile).setWorldTankId(tankId);
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, @Nullable World worldIn, List<ITextComponent> list, ITooltipFlag flagIn) {
        super.appendHoverText(itemStack, worldIn, list, flagIn);
        ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(itemStack).orElse(null);
        String tankId = fluidHandler == null ? "null" : fluidHandler.getTankID();
        list.add(new TranslationTextComponent("block.evilcraft.entangled_chalice.info.id", tankIdToNameParts(tankId)));
    }

    public static String tankIdToNameParts(String tankId) {
        try {
            int i = Integer.parseInt(tankId);
            String a = namePartsArray[(i + 3) % namePartsArray.length];
            String b = namePartsArray[(i * 3 + 5) % namePartsArray.length];
            String c = namePartsArray[(i * 13 + 7) % namePartsArray.length];
            return String.format("%s %s %s", a, b, c);
        } catch (NumberFormatException e) {
            return tankId;
        }
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

        @Override
        protected void setContainerToEmpty() {
            setFluid(FluidStack.EMPTY);
        }

        /**
         * Get the tank id from the container.
         * @return The tank id.
         */
        public String getTankID() {
            CompoundNBT tag = getContainer().getTag();
            if(tag != null) {
                if (!tag.contains(WorldSharedTank.NBT_TANKID)) {
                    tag.putString(WorldSharedTank.NBT_TANKID, "");
                }
                return tag.getString(WorldSharedTank.NBT_TANKID);
            }
            return "";
        }

        /**
         * Set the tank id for the container.
         * @param tankID The tank id.
         */
        public void setTankID(String tankID) {
            CompoundNBT tag = getContainer().getOrCreateTag();
            tag.putString(WorldSharedTank.NBT_TANKID, tankID);
        }

        /**
         * Set a new unique tank id for the container.
         */
        public void setNextTankID() {
            setTankID(Integer.toString(EvilCraft.globalCounters.getNext("EntangledChalice")));
        }
    }
}
