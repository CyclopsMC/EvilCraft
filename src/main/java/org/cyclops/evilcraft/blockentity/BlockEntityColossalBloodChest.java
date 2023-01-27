package org.cyclops.evilcraft.blockentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
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
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.cyclops.cyclopscore.block.multi.AllowedBlock;
import org.cyclops.cyclopscore.block.multi.CubeDetector;
import org.cyclops.cyclopscore.block.multi.ExactBlockCountValidator;
import org.cyclops.cyclopscore.block.multi.ExactSizeValidator;
import org.cyclops.cyclopscore.block.multi.HollowCubeDetector;
import org.cyclops.cyclopscore.capability.item.ItemHandlerSlotMasked;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockColossalBloodChestConfig;
import org.cyclops.evilcraft.blockentity.tickaction.EmptyFluidContainerInTankTickAction;
import org.cyclops.evilcraft.blockentity.tickaction.bloodchest.BulkRepairItemTickAction;
import org.cyclops.evilcraft.core.blockentity.BlockEntityWorking;
import org.cyclops.evilcraft.core.blockentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.blockentity.tickaction.TickComponent;
import org.cyclops.evilcraft.core.blockentity.upgrade.IUpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.blockentity.upgrade.UpgradeBehaviour;
import org.cyclops.evilcraft.core.blockentity.upgrade.Upgrades;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.inventory.container.ContainerColossalBloodChest;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * A machine that can infuse things with blood.
 * @author rubensworks
 *
 */
public class BlockEntityColossalBloodChest extends BlockEntityWorking<BlockEntityColossalBloodChest, MutableFloat> implements MenuProvider, LidBlockEntity {

    private static final int TICK_MODULUS = 200;

    /**
     * The amount of slots in the chest.
     */
    public static final int SLOTS_CHEST = ContainerColossalBloodChest.CHEST_INVENTORY_ROWS * ContainerColossalBloodChest.CHEST_INVENTORY_COLUMNS;
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

    public static final int MAX_EFFICIENCY = 200;

    public static Metadata METADATA = new Metadata();

    @NBTPersist(useDefaultValue = false)
    private Vec3i size = LocationHelpers.copyLocation(Vec3i.ZERO);
    @NBTPersist(useDefaultValue = false)
    private Vec3i renderOffset = new Vec3i(0, 0, 0);
    @Getter
    @Setter
    @NBTPersist
    private Integer efficiency = 0;
    @NBTPersist
    private int rotation = 0;
    private int repairTicker;
    @Getter
    private final Map<Integer, Boolean> slotTickHistory;

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        protected void onOpen(Level level, BlockPos pos, BlockState blockState) {
            BlockEntityColossalBloodChest.playSound(level, pos, blockState, SoundEvents.CHEST_OPEN);
        }

        protected void onClose(Level level, BlockPos pos, BlockState blockState) {
            BlockEntityColossalBloodChest.playSound(level, pos, blockState, SoundEvents.CHEST_CLOSE);
        }

        protected void openerCountChanged(Level level, BlockPos pos, BlockState blockState, int p_155364_, int p_155365_) {
            BlockEntityColossalBloodChest.this.signalOpenCount(level, pos, blockState, p_155364_, p_155365_);
        }

        protected boolean isOwnContainer(Player player) {
            if (!(player.containerMenu instanceof ContainerColossalBloodChest)) {
                return false;
            } else {
                Container container = ((ContainerColossalBloodChest)player.containerMenu).getContainerInventory();
                return container == BlockEntityColossalBloodChest.this.getInventory();
            }
        }
    };
    private final ChestLidController chestLidController = new ChestLidController();

    public static final Upgrades.UpgradeEventType UPGRADEEVENT_SPEED = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_BLOODUSAGE = Upgrades.newUpgradeEventType();

    private static final Map<Class<?>, ITickAction<BlockEntityColossalBloodChest>> REPAIR_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<BlockEntityColossalBloodChest>>();
    static {
        REPAIR_TICK_ACTIONS.put(Item.class, new BulkRepairItemTickAction());
    }

    private static final Map<Class<?>, ITickAction<BlockEntityColossalBloodChest>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<BlockEntityColossalBloodChest>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(Item.class, new EmptyFluidContainerInTankTickAction<BlockEntityColossalBloodChest>());
    }
    public static int TICKERS = SLOTS_CHEST + 1;

    protected static final ExactSizeValidator exactSizeValidator = new ExactSizeValidator(new Vec3i(2, 2, 2));

    private static CubeDetector detector;

    /**
     * Make a new instance.
     */
    public BlockEntityColossalBloodChest(BlockPos blockPos, BlockState blockState) {
        super(
                RegistryEntries.BLOCK_ENTITY_COLOSSAL_BLOOD_CHEST,
                blockPos,
                blockState,
                SLOTS,
                64,
                LIQUID_PER_SLOT,
                RegistryEntries.FLUID_BLOOD);
        for(int i = 0; i < SLOTS_CHEST; i++) {
            addTicker(
                    new TickComponent<
                            BlockEntityColossalBloodChest,
                            ITickAction<BlockEntityColossalBloodChest>
                            >(this, REPAIR_TICK_ACTIONS, i)
            );
        }
        addTicker(
                new TickComponent<
                        BlockEntityColossalBloodChest,
                        ITickAction<BlockEntityColossalBloodChest>
                        >(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false, true)
        );
        assert getTickers().size() == TICKERS;

        // Upgrade behaviour
        upgradeBehaviour.put(Upgrades.UPGRADE_EFFICIENCY, new UpgradeBehaviour<BlockEntityColossalBloodChest, MutableFloat>(2) {
            @Override
            public void applyUpgrade(BlockEntityColossalBloodChest upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableFloat> event) {
                if(event.getType() == UPGRADEEVENT_BLOODUSAGE) {
                    float val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
        upgradeBehaviour.put(Upgrades.UPGRADE_SPEED, new UpgradeBehaviour<BlockEntityColossalBloodChest, MutableFloat>(1) {
            @Override
            public void applyUpgrade(BlockEntityColossalBloodChest upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableFloat> event) {
                if(event.getType() == UPGRADEEVENT_SPEED) {
                    float val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });

        slotTickHistory = Maps.newHashMap();
        resetSlotHistory();
    }

    public static CubeDetector getCubeDetector() {
        if (detector == null) {
            detector = new HollowCubeDetector(
                    new AllowedBlock[]{
                            new AllowedBlock(RegistryEntries.BLOCK_REINFORCED_UNDEAD_PLANKS),
                            new AllowedBlock(RegistryEntries.BLOCK_COLOSSAL_BLOOD_CHEST).addCountValidator(new ExactBlockCountValidator(1))
                    },
                    Lists.newArrayList(RegistryEntries.BLOCK_COLOSSAL_BLOOD_CHEST, RegistryEntries.BLOCK_REINFORCED_UNDEAD_PLANKS)
            ).addSizeValidator(exactSizeValidator);
        }
        return detector;
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

    protected void resetSlotHistory() {
        for(int i = 0; i < getBasicInventorySize(); i++) {
            slotTickHistory.put(i, false);
        }
    }

    @Override
    protected SingleUseTank createTank(int tankSize) {
        return new ImplicitFluidConversionTank(tankSize, BloodFluidConverter.getInstance());
    }

    /**
     * @return If the structure is valid.
     */
    public boolean isStructureComplete() {
        return !getSize().equals(Vec3i.ZERO);
    }

    public int getSizeSingular() {
        return getSize().getX() + 1;
    }

    /**
     * @return the size
     */
    public Vec3i getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Vec3i size) {
        this.size = size;
        sendUpdate();
    }

    @Override
    public Metadata getTileWorkingMetadata() {
        return METADATA;
    }

    @Override
    public boolean canWork() {
        Vec3i size = getSize();
        return size.compareTo(exactSizeValidator.getExactSize()) == 0;
    }

    /**
     * Check if the spirit furnace on the given location is valid and can start working.
     * @param world The world.
     * @param location The location.
     * @return If it is valid.
     */
    public static boolean canWork(Level world, BlockPos location) {
        return BlockEntityHelpers.get(world, location, BlockEntityColossalBloodChest.class)
                .map(BlockEntityColossalBloodChest::canWork)
                .orElse(false);
    }

    @Override
    protected int getWorkTicker() {
        return repairTicker;
    }

    @Override
    public int getNewState() {
        return 0;
    }

    @Override
    public void onStateChanged() {

    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new Inventory<BlockEntityColossalBloodChest>(inventorySize, stackSize, this) {
            @Override
            public void startOpen(Player entityPlayer) {
                super.startOpen(entityPlayer);
                BlockEntityColossalBloodChest.this.startOpen(entityPlayer);
            }

            @Override
            public void stopOpen(Player entityPlayer) {
                super.stopOpen(entityPlayer);
                BlockEntityColossalBloodChest.this.stopOpen(entityPlayer);
            }

            @Override
            public boolean stillValid(Player entityPlayer) {
                return super.stillValid(entityPlayer)
                        && !(level == null || level.getBlockEntity(getBlockPos()) != tile);
            }

            @Override
            public boolean canPlaceItem(int slot, ItemStack itemStack) {
                if(slot == SLOT_CONTAINER)
                    return SlotFluidContainer.checkIsItemValid(itemStack, RegistryEntries.FLUID_BLOOD);
                else if(slot <= SLOTS_CHEST && slot >= 0)
                    return SlotRepairable.checkIsItemValid(itemStack);
                return super.canPlaceItem(slot, itemStack);
            }
        };
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

    public static void lidAnimateTick(Level level, BlockPos pos, BlockState blockState, BlockEntityColossalBloodChest blockEntity) {
        blockEntity.chestLidController.tickLid();
    }

    @Override
    public float getOpenNess(float value) {
        return this.chestLidController.getOpenness(value);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos().subtract(new Vec3i(3, 3, 3)), getBlockPos().offset(3, 6, 3));
    }

    public void setCenter(BlockPos center) {
        Direction rotation = Direction.NORTH;
        if(center.getX() != getBlockPos().getX()) {
            rotation = DirectionHelpers.getEnumFacingFromXSign(center.getX() - getBlockPos().getX());
        } else if(center.getZ() != getBlockPos().getZ()) {
            rotation = DirectionHelpers.getEnumFacingFromZSing(center.getZ() - getBlockPos().getZ());
        }
        this.setRotation(rotation);
        this.renderOffset = getBlockPos().subtract(center);
    }

    public void setRotation(Direction rotation) {
        this.rotation = rotation.ordinal();
    }

    @Override
    public Direction getRotation() {
        return Direction.from3DDataValue(this.rotation);
    }

    public Vec3i getRenderOffset() {
        return this.renderOffset;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory playerInventory, Player playerEntity) {
        return new ContainerColossalBloodChest(id, playerInventory, this.getInventory(), Optional.of(this));
    }

    /**
     * Callback for when a structure has been detected for a spirit furnace block.
     * @param world The world.
     * @param location The location of one block of the structure.
     * @param size The size of the structure.
     * @param valid If the structure is being validated(/created), otherwise invalidated.
     * @param originCorner The origin corner
     */
    public static void detectStructure(Level world, BlockPos location, Vec3i size, boolean valid, BlockPos originCorner) {

    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.evilcraft.colossal_blood_chest");
    }

    private static class Metadata extends BlockEntityWorking.Metadata {
        private Metadata() {
            super(SLOTS);
        }

        @Override
        public boolean canConsume(ItemStack itemStack, Level world) {
            return SlotRepairable.checkIsItemValid(itemStack);
        }

        @Override
        protected Block getBlock() {
            return RegistryEntries.BLOCK_COLOSSAL_BLOOD_CHEST;
        }
    }

    public static class TickerServer extends BlockEntityWorking.TickerServer<BlockEntityColossalBloodChest, MutableFloat> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityColossalBloodChest blockEntity) {
            blockEntity.resetSlotHistory();

            super.update(level, pos, blockState, blockEntity);

            if (level != null&& level.getGameTime() % BlockColossalBloodChestConfig.ticksPerDamage == 0) {
                int oldEfficiency = blockEntity.efficiency;
                blockEntity.efficiency = Math.max(0, blockEntity.efficiency - BlockColossalBloodChestConfig.baseConcurrentItems);
                if(oldEfficiency != blockEntity.efficiency) {
                    blockEntity.setChanged();
                }
            }
        }
    }

    public static class TickerClient extends BlockEntityWorking.TickerClient<BlockEntityColossalBloodChest> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityColossalBloodChest blockEntity) {
            super.update(level, pos, blockState, blockEntity);
            BlockEntityColossalBloodChest.lidAnimateTick(level, pos, blockState, blockEntity);
        }
    }

}
