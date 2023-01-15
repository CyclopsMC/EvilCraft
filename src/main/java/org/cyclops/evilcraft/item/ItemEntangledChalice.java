package org.cyclops.evilcraft.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
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
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.cyclops.evilcraft.core.fluid.FluidContainerItemWrapperWithSimulation;
import org.cyclops.evilcraft.core.fluid.WorldSharedTank;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import javax.annotation.Nullable;
import java.util.List;

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
    protected void autofill(int itemSlot, IFluidHandlerItem source, Level world, Entity entity) {
        if(entity instanceof Player && !world.isClientSide()) {
            Player player = (Player) entity;
            FluidStack tickFluid;
            PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
            do {
                tickFluid = FluidHelpers.getFluid(source);
                ItemStack toFill = it.next();
                if (tickFluid != null && !toFill.isEmpty() && toFill.getCount() == 1) {
                    ItemStack filled = ItemHelpers.tryFillContainerForPlayer(source, toFill, tickFluid, player);
                    if (!filled.isEmpty()) {
                        it.replace(filled);
                        player.getInventory().setItem(itemSlot, source.getContainer());
                    }
                }
            } while(tickFluid != null && tickFluid.getAmount() > 0 && it.hasNext());
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new FluidHandler(stack, BlockEntityEntangledChalice.BASE_CAPACITY);
    }

    @Override
    protected boolean itemStackDataToTile(ItemStack itemStack, BlockEntity tile) {
        super.itemStackDataToTile(itemStack, tile);
        // Convert tank id
        ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(itemStack).orElse(null);
        String tankId = fluidHandler.getTankID();
        ((BlockEntityEntangledChalice) tile).setWorldTankId(tankId);
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemStack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
        super.appendHoverText(itemStack, worldIn, list, flagIn);
        ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) FluidUtil.getFluidHandler(itemStack).orElse(null);
        String tankId = fluidHandler == null ? "null" : fluidHandler.getTankID();
        list.add(Component.translatable("block.evilcraft.entangled_chalice.info.id", tankIdToNameParts(tankId)));
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
            CompoundTag tag = getContainer().getTag();
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
            CompoundTag tag = getContainer().getOrCreateTag();
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
