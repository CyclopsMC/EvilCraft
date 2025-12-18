package org.cyclops.evilcraft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.inventory.ItemAccessItemLocation;
import org.cyclops.cyclopscore.inventory.ItemLocation;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockEntangledChalice;
import org.cyclops.evilcraft.blockentity.BlockEntityEntangledChalice;
import org.cyclops.evilcraft.core.fluid.WorldSharedTankCache;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.item.ItemBlockFluidContainer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

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
    protected void autofill(@Nullable EquipmentSlot itemSlot, ResourceHandler<FluidResource> source, ItemStack sourceItem, Level world, Entity entity) {
        if(entity instanceof Player && !world.isClientSide()) {
            Player player = (Player) entity;
            FluidStack tickFluid;
            PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player);
            do {
                tickFluid = IModHelpersNeoForge.get().getFluidHelpers().getFluid(source);
                ItemLocation toFillLocation = it.nextIndexed();
                ItemStack toFill = toFillLocation.getItemStack(player);
                if (tickFluid != null && !toFill.isEmpty() && toFill.getCount() == 1) {
                    ItemStack filled = ItemHelpers.tryFillContainerForPlayer(source, sourceItem, new ItemAccessItemLocation(player, toFillLocation), toFill, tickFluid, player);
                    if (!filled.isEmpty()) {
                        it.replace(filled);
                    }
                }
            } while(tickFluid != null && tickFluid.getAmount() > 0 && it.hasNext());
        }
    }

    @Override
    protected boolean itemStackDataToTile(ItemStack itemStack, BlockEntity tile) {
        super.itemStackDataToTile(itemStack, tile);
        // Convert tank id
        ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));
        String tankId = fluidHandler.getTankID();
        ((BlockEntityEntangledChalice) tile).setWorldTankId(tankId);
        return true;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(itemStack, context, tooltipDisplay, tooltipAdder, flag);
        ItemEntangledChalice.FluidHandler fluidHandler = (ItemEntangledChalice.FluidHandler) itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));
        String tankId = fluidHandler == null ? "null" : fluidHandler.getTankID();
        tooltipAdder.accept(Component.translatable("block.evilcraft.entangled_chalice.info.id", tankIdToNameParts(tankId)));
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

    public static class FluidHandler extends ItemAccessResourceHandler<FluidResource> {

        private final Journal rootCommitJournal;

        private FluidStack lastFluid = FluidStack.EMPTY;

        public FluidHandler(ItemAccess itemAccess) {
            super(itemAccess, 1);
            this.rootCommitJournal = new Journal();
        }

        protected FluidStack getFluid() {
            return WorldSharedTankCache.getInstance().getTankContent(getTankID());
        }

        @Override
        protected FluidResource getResourceFrom(ItemResource accessResource, int index) {
            return FluidResource.of(getFluid());
        }

        @Override
        protected int getAmountFrom(ItemResource accessResource, int index) {
            return getFluid().getAmount();
        }

        @Override
        protected ItemResource update(ItemResource itemResource, int index, FluidResource resource, int newAmount) {
            this.lastFluid = resource.toStack(newAmount);
            return itemResource;
        }

        @Override
        protected int getCapacity(int i, FluidResource resource) {
            return BlockEntityEntangledChalice.BASE_CAPACITY;
        }

        @Override
        public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
            this.rootCommitJournal.updateSnapshots(transaction);
            return super.insert(index, resource, amount, transaction);
        }

        @Override
        public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
            this.rootCommitJournal.updateSnapshots(transaction);
            return super.extract(index, resource, amount, transaction);
        }

        /**
         * Get the tank id from the container.
         * @return The tank id.
         */
        public String getTankID() {
            return itemAccess.getResource().getOrDefault(RegistryEntries.COMPONENT_WORLD_SHARED_TANK_ID, "");
        }

        /**
         * Set the tank id for the container.
         * @param tankID The tank id.
         */
        public void setTankID(String tankID, TransactionContext transaction) {
            itemAccess.exchange(itemAccess.getResource()
                    .with(RegistryEntries.COMPONENT_WORLD_SHARED_TANK_ID, tankID), itemAccess.getAmount(), transaction);
        }

        /**
         * Set a new unique tank id for the container.
         */
        public void setNextTankID(TransactionContext transaction) {
            setTankID(Integer.toString(EvilCraft.globalCounters.get().getNext("EntangledChalice")), transaction);
        }

        public class Journal extends SnapshotJournal<FluidStack> {

            @Override
            protected FluidStack createSnapshot() {
                return lastFluid.copy();
            }

            @Override
            protected void revertToSnapshot(FluidStack fluidStack) {
                lastFluid = fluidStack;
            }

            @Override
            protected void onRootCommit(FluidStack originalState) {
                super.onRootCommit(originalState);
                WorldSharedTankCache.getInstance().setTankContent(getTankID(), lastFluid);
            }
        }
    }
}
