package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.cyclops.cyclopscore.block.multi.AllowedBlock;
import org.cyclops.cyclopscore.block.multi.CubeDetector;
import org.cyclops.cyclopscore.block.multi.ExactBlockCountValidator;
import org.cyclops.cyclopscore.block.multi.ExactSizeValidator;
import org.cyclops.cyclopscore.block.multi.HollowCubeDetector;
import org.cyclops.cyclopscore.capability.item.ItemHandlerSlotMasked;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.cyclopscore.inventory.SimpleInventory;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockColossalBloodChestConfig;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.core.tileentity.TileWorking;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;
import org.cyclops.evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeBehaviour;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.inventory.container.ContainerColossalBloodChest;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;
import org.cyclops.evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.BulkRepairItemTickAction;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A machine that can infuse things with blood.
 * @author rubensworks
 *
 */
public class TileColossalBloodChest extends TileWorking<TileColossalBloodChest, MutableFloat> implements INamedContainerProvider, IChestLid {

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
    private Vector3i size = LocationHelpers.copyLocation(Vector3i.NULL_VECTOR);
    @NBTPersist(useDefaultValue = false)
    private Vector3i renderOffset = new Vector3i(0, 0, 0);
    @Getter
    @Setter
    @NBTPersist
    private Integer efficiency = 0;
    @NBTPersist
    private int rotation = 0;
    private int repairTicker;
    /**
     * The previous angle of the lid.
     */
    public float prevLidAngle;
    /**
     * The current angle of the lid.
     */
    public float lidAngle;
    private int playersUsing;
    @Getter
    private final Map<Integer, Boolean> slotTickHistory;

    public static final Upgrades.UpgradeEventType UPGRADEEVENT_SPEED = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_BLOODUSAGE = Upgrades.newUpgradeEventType();

    private static final Map<Class<?>, ITickAction<TileColossalBloodChest>> REPAIR_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileColossalBloodChest>>();
    static {
        REPAIR_TICK_ACTIONS.put(Item.class, new BulkRepairItemTickAction());
    }

    private static final Map<Class<?>, ITickAction<TileColossalBloodChest>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileColossalBloodChest>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(Item.class, new EmptyFluidContainerInTankTickAction<TileColossalBloodChest>());
    }
    public static int TICKERS = SLOTS_CHEST + 1;

    protected static final ExactSizeValidator exactSizeValidator = new ExactSizeValidator(new Vector3i(2, 2, 2));

    private static CubeDetector detector;

    /**
     * Make a new instance.
     */
    public TileColossalBloodChest() {
        super(
                RegistryEntries.TILE_ENTITY_COLOSSAL_BLOOD_CHEST,
                SLOTS,
                64,
                LIQUID_PER_SLOT,
                RegistryEntries.FLUID_BLOOD);
        for(int i = 0; i < SLOTS_CHEST; i++) {
            addTicker(
                    new TickComponent<
                            TileColossalBloodChest,
                            ITickAction<TileColossalBloodChest>
                            >(this, REPAIR_TICK_ACTIONS, i)
            );
        }
        addTicker(
                new TickComponent<
                        TileColossalBloodChest,
                        ITickAction<TileColossalBloodChest>
                        >(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false, true)
        );
        assert getTickers().size() == TICKERS;

        // Upgrade behaviour
        upgradeBehaviour.put(Upgrades.UPGRADE_EFFICIENCY, new UpgradeBehaviour<TileColossalBloodChest, MutableFloat>(2) {
            @Override
            public void applyUpgrade(TileColossalBloodChest upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
                                     IUpgradeSensitiveEvent<MutableFloat> event) {
                if(event.getType() == UPGRADEEVENT_BLOODUSAGE) {
                    float val = event.getObject().getValue();
                    val /= (1 + upgradeLevel / valueFactor);
                    event.getObject().setValue(val);
                }
            }
        });
        upgradeBehaviour.put(Upgrades.UPGRADE_SPEED, new UpgradeBehaviour<TileColossalBloodChest, MutableFloat>(1) {
            @Override
            public void applyUpgrade(TileColossalBloodChest upgradable, Upgrades.Upgrade upgrade, int upgradeLevel,
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
        LazyOptional<IItemHandler> itemHandlerChest = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOTS_CHEST));
        LazyOptional<IItemHandler> itemHandlerContainer = LazyOptional.of(() -> new ItemHandlerSlotMasked(getInventory(), SLOT_CONTAINER));
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP, itemHandlerChest);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN, itemHandlerChest);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.NORTH, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.SOUTH, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.WEST, itemHandlerContainer);
        addCapabilitySided(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.EAST, itemHandlerContainer);
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
        return !getSize().equals(Vector3i.NULL_VECTOR);
    }

    public int getSizeSingular() {
        return getSize().getX() + 1;
    }

    /**
     * @return the size
     */
    public Vector3i getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Vector3i size) {
        this.size = size;
        sendUpdate();
    }

    @Override
    public Metadata getTileWorkingMetadata() {
        return METADATA;
    }

    @Override
    public boolean canWork() {
        Vector3i size = getSize();
        return size.compareTo(exactSizeValidator.getExactSize()) == 0;
    }

    /**
     * Check if the spirit furnace on the given location is valid and can start working.
     * @param world The world.
     * @param location The location.
     * @return If it is valid.
     */
    public static boolean canWork(World world, BlockPos location) {
        return TileHelpers.getSafeTile(world, location, TileColossalBloodChest.class)
                .map(TileColossalBloodChest::canWork)
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
    public void updateTileEntity() {
        resetSlotHistory();
        super.updateTileEntity();
        if (world != null && !this.world.isRemote() && this.world.getGameTime() % BlockColossalBloodChestConfig.ticksPerDamage == 0) {
            int oldEfficiency = efficiency;
            efficiency = Math.max(0, efficiency - BlockColossalBloodChestConfig.baseConcurrentItems);
            if(oldEfficiency != efficiency) {
                markDirty();
            }
        }
        // Resynchronize clients with the server state, the last condition makes sure
        // not all chests are synced at the same time.
        if (world != null
                && !this.world.isRemote()
                && this.playersUsing != 0
                && WorldHelpers.efficientTick(world, TICK_MODULUS, getPos().hashCode())) {
            this.playersUsing = 0;
            float range = 5.0F;
            @SuppressWarnings("unchecked")
            List<PlayerEntity> entities = this.world.getEntitiesWithinAABB(
                    PlayerEntity.class,
                    new AxisAlignedBB(
                            getPos().subtract(new Vector3i(range, range, range)),
                            getPos().add(new Vector3i(1 + range, 1 + range, 1 + range))
                    )
            );

            for(PlayerEntity player : entities) {
                if (player.openContainer instanceof ContainerColossalBloodChest) {
                    ++this.playersUsing;
                }
            }

            world.addBlockEvent(getPos(), getBlockState().getBlock(), 1, playersUsing);
        }

        prevLidAngle = lidAngle;
        float increaseAngle = 0.05F;
        if (playersUsing > 0 && lidAngle == 0.0F) {
            world.playSound(
                    (double) getPos().getX() + 0.5D,
                    (double) getPos().getY() + 0.5D,
                    (double) getPos().getZ() + 0.5D,
                    SoundEvents.BLOCK_CHEST_OPEN,
                    SoundCategory.BLOCKS,
                    0.5F,
                    world.rand.nextFloat() * 0.1F + 0.5F,
                    false
            );
        }
        if (playersUsing == 0 && lidAngle > 0.0F || playersUsing > 0 && lidAngle < 1.0F) {
            float preIncreaseAngle = lidAngle;
            if (playersUsing > 0) {
                lidAngle += increaseAngle;
            } else {
                lidAngle -= increaseAngle;
            }
            if (lidAngle > 1.0F) {
                lidAngle = 1.0F;
            }
            float closedAngle = 0.5F;
            if (lidAngle < closedAngle && preIncreaseAngle >= closedAngle) {
                world.playSound(
                        (double) getPos().getX() + 0.5D,
                        (double) getPos().getY() + 0.5D,
                        (double) getPos().getZ() + 0.5D,
                        SoundEvents.BLOCK_CHEST_CLOSE,
                        SoundCategory.BLOCKS,
                        0.5F,
                        world.rand.nextFloat() * 0.1F + 0.5F,
                        false
                );
            }
            if (lidAngle < 0.0F) {
                lidAngle = 0.0F;
            }
        }
    }

    @Override
    public boolean receiveClientEvent(int i, int j) {
        if (i == 1) {
            playersUsing = j;
        }
        return true;
    }

    @Override
    protected SimpleInventory createInventory(int inventorySize, int stackSize) {
        return new Inventory<TileColossalBloodChest>(inventorySize, stackSize, this) {
            @Override
            public void openInventory(PlayerEntity entityPlayer) {
                triggerPlayerUsageChange(1);
            }

            @Override
            public void closeInventory(PlayerEntity entityPlayer) {
                triggerPlayerUsageChange(-1);
            }

            @Override
            public boolean isUsableByPlayer(PlayerEntity entityPlayer) {
                return super.isUsableByPlayer(entityPlayer)
                        && !(world == null || world.getTileEntity(getPos()) != tile);
            }

            @Override
            public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
                if(slot == SLOT_CONTAINER)
                    return SlotFluidContainer.checkIsItemValid(itemStack, RegistryEntries.FLUID_BLOOD);
                else if(slot <= SLOTS_CHEST && slot >= 0)
                    return SlotRepairable.checkIsItemValid(itemStack);
                return super.isItemValidForSlot(slot, itemStack);
            }
        };
    }

    private void triggerPlayerUsageChange(int change) {
        if (world != null) {
            playersUsing += change;
            world.addBlockEvent(getPos(), getBlockState().getBlock(), 1, playersUsing);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos().subtract(new Vector3i(3, 3, 3)), getPos().add(3, 6, 3));
    }

    public void setCenter(BlockPos center) {
        Direction rotation = Direction.NORTH;
        if(center.getX() != getPos().getX()) {
            rotation = DirectionHelpers.getEnumFacingFromXSign(center.getX() - getPos().getX());
        } else if(center.getZ() != getPos().getZ()) {
            rotation = DirectionHelpers.getEnumFacingFromZSing(center.getZ() - getPos().getZ());
        }
        this.setRotation(rotation);
        this.renderOffset = getPos().subtract(center);
    }

    public void setRotation(Direction rotation) {
        this.rotation = rotation.ordinal();
    }

    @Override
    public Direction getRotation() {
        return Direction.byIndex(this.rotation);
    }

    public Vector3i getRenderOffset() {
        return this.renderOffset;
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
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
    public static void detectStructure(World world, BlockPos location, Vector3i size, boolean valid, BlockPos originCorner) {

    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.evilcraft.colossal_blood_chest");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }

    private static class Metadata extends TileWorking.Metadata {
        private Metadata() {
            super(SLOTS);
        }

        @Override
        public boolean canConsume(ItemStack itemStack, World world) {
            return SlotRepairable.checkIsItemValid(itemStack);
        }

        @Override
        protected Block getBlock() {
            return RegistryEntries.BLOCK_COLOSSAL_BLOOD_CHEST;
        }
    }

}
