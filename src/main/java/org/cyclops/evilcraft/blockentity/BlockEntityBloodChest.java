package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.cyclopscore.capability.item.ItemHandlerSlotMasked;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.tickaction.EmptyFluidContainerInTankTickAction;
import org.cyclops.evilcraft.blockentity.tickaction.bloodchest.RepairItemTickAction;
import org.cyclops.evilcraft.core.blockentity.BlockEntityTickingTankInventory;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.blockentity.tickaction.TickComponent;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.inventory.container.ContainerBloodChest;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * A chest that is able to repair tools with the use of blood.
 * Partially based on cpw's IronChests.
 * @author rubensworks
 *
 */
public class BlockEntityBloodChest extends BlockEntityTickingTankInventory<BlockEntityBloodChest> implements MenuProvider, LidBlockEntity {

    private static final int TICK_MODULUS = 200;

    /**
     * The amount of slots in the chest.
     */
    public static final int SLOTS_CHEST = ContainerBloodChest.CHEST_INVENTORY_ROWS * ContainerBloodChest.CHEST_INVENTORY_COLUMNS;
    /**
     * The total amount of slots.
     */
    public static final int SLOTS = SLOTS_CHEST + 1;
    /**
     * The id of the fluid container slot.
     */
    public static final int SLOT_CONTAINER = SLOTS - 1;

    /**
     * The capacity of the tank.
     */
    public static final int LIQUID_PER_SLOT = FluidHelpers.BUCKET_VOLUME * 10;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
            BlockEntityBloodChest.playSound(level, pos, blockState, SoundEvents.CHEST_OPEN);
        }

        protected void onClose(Level level, BlockPos pos, BlockState blockState) {
            BlockEntityBloodChest.playSound(level, pos, blockState, SoundEvents.CHEST_CLOSE);
        }

        protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int p_155364_, int p_155365_) {
            BlockEntityBloodChest.this.signalOpenCount(level, pos, blockState, p_155364_, p_155365_);
        }

        protected boolean isOwnContainer(Player player) {
            if (!(player.containerMenu instanceof ContainerBloodChest)) {
                return false;
            } else {
                Container container = ((ContainerBloodChest)player.containerMenu).getContainerInventory();
                return container == BlockEntityBloodChest.this.getInventory();
            }
        }
    };
    private final ChestLidController chestLidController = new ChestLidController();

    private static final Map<Class<?>, ITickAction<BlockEntityBloodChest>> REPAIR_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<BlockEntityBloodChest>>();
    static {
        REPAIR_TICK_ACTIONS.put(Item.class, new RepairItemTickAction());
    }

    private static final Map<Class<?>, ITickAction<BlockEntityBloodChest>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<BlockEntityBloodChest>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(Item.class, new EmptyFluidContainerInTankTickAction<BlockEntityBloodChest>());
    }

    public BlockEntityBloodChest(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_BLOOD_CHEST, blockPos, blockState, SLOTS, 64, LIQUID_PER_SLOT, RegistryEntries.FLUID_BLOOD);
        for(int i = 0; i < SLOTS_CHEST; i++) {
            addTicker(new TickComponent<>(this, REPAIR_TICK_ACTIONS, i));
        }
        addTicker(new TickComponent<>(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false, true));
    }

    @Override
    protected void addItemHandlerCapabilities() {
        LazyOptional<IItemHandler> itemHandlerChest = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), IntStream.range(0, SLOTS_CHEST).toArray()));
        LazyOptional<IItemHandler> itemHandlerContainer = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_CONTAINER));
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.UP, itemHandlerChest);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN, itemHandlerChest);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.NORTH, itemHandlerContainer);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.SOUTH, itemHandlerContainer);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.WEST, itemHandlerContainer);
        addCapabilitySided(ForgeCapabilities.ITEM_HANDLER, Direction.EAST, itemHandlerContainer);
    }

    @Override
    public Direction getRotation() {
        // World can be null during world loading
        if (getLevel() == null) {
            return Direction.SOUTH;
        }
        return BlockHelpers.getSafeBlockStateProperty(getBlockState(), org.cyclops.evilcraft.block.BlockBloodChest.FACING, Direction.SOUTH).getOpposite();
    }

    @Override
    protected SingleUseTank createTank(int tankSize) {
        return new ImplicitFluidConversionTank(tankSize, BloodFluidConverter.getInstance());
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new SimpleInventory(inventorySize, stackSize) {
            @Override
            public boolean canPlaceItem(int slot, ItemStack itemstack) {
                if(slot == SLOT_CONTAINER)
                    return SlotFluidContainer.checkIsItemValid(itemstack, RegistryEntries.FLUID_BLOOD);
                else if(slot <= SLOTS_CHEST && slot >= 0)
                    return SlotRepairable.checkIsItemValid(itemstack);
                return false;
            }

            @Override
            public void startOpen(Player entityPlayer) {
                super.startOpen(entityPlayer);
                BlockEntityBloodChest.this.startOpen(entityPlayer);
            }

            @Override
            public void stopOpen(Player entityPlayer) {
                super.stopOpen(entityPlayer);
                BlockEntityBloodChest.this.stopOpen(entityPlayer);
            }

            @Override
            public boolean stillValid(Player entityPlayer) {
                return super.stillValid(entityPlayer)
                        && !(level == null || level.getBlockEntity(getBlockPos()) != BlockEntityBloodChest.this);
            }
        };
    }

    @Override
    public int getNewState() {
        return 0;
    }

    @Override
    public void onStateChanged() {

    }

    static void playSound(Level level, BlockPos pos, BlockState blockState, SoundEvent soundEvent) {
        level.playSound((Player)null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, soundEvent, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public boolean triggerEvent(int eventType, int value) {
        if (eventType == 1) {
            this.chestLidController.shouldBeOpen(value > 0);
            return true;
        } else {
            return super.triggerEvent(eventType, value);
        }
    }

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    protected void signalOpenCount(Level level, BlockPos pos, BlockState blockState, int p_155336_, int value) {
        Block block = blockState.getBlock();
        level.blockEvent(pos, block, 1, value);
    }

    public static void lidAnimateTick(Level level, BlockPos pos, BlockState blockState, BlockEntityBloodChest blockEntity) {
        blockEntity.chestLidController.tickLid();
    }

    @Override
    public float getOpenNess(float value) {
        return this.chestLidController.getOpenness(value);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new ContainerBloodChest(id, playerInventory, this.getInventory(), Optional.of(this));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.evilcraft.blood_chest");
    }

    public static class TickerClient extends BlockEntityTickingTankInventory.TickerClient<BlockEntityBloodChest> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityBloodChest blockEntity) {
            super.update(level, pos, blockState, blockEntity);
            BlockEntityBloodChest.lidAnimateTick(level, pos, blockState, blockEntity);
        }
    }
}
