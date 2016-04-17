package org.cyclops.evilcraft.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.cyclops.cyclopscore.fluid.SingleUseTank;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.cyclopscore.inventory.slot.SlotFluidContainer;
import org.cyclops.cyclopscore.persist.nbt.NBTPersist;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.block.ColossalBloodChest;
import org.cyclops.evilcraft.block.ColossalBloodChestConfig;
import org.cyclops.evilcraft.block.ReinforcedUndeadPlank;
import org.cyclops.evilcraft.core.block.AllowedBlock;
import org.cyclops.evilcraft.core.block.CubeDetector;
import org.cyclops.evilcraft.core.block.HollowCubeDetector;
import org.cyclops.evilcraft.core.fluid.BloodFluidConverter;
import org.cyclops.evilcraft.core.fluid.ImplicitFluidConversionTank;
import org.cyclops.evilcraft.core.tileentity.tickaction.ITickAction;
import org.cyclops.evilcraft.core.tileentity.tickaction.TickComponent;
import org.cyclops.evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import org.cyclops.evilcraft.core.tileentity.upgrade.UpgradeBehaviour;
import org.cyclops.evilcraft.core.tileentity.upgrade.Upgrades;
import org.cyclops.evilcraft.fluid.Blood;
import org.cyclops.evilcraft.inventory.container.ContainerColossalBloodChest;
import org.cyclops.evilcraft.inventory.slot.SlotRepairable;
import org.cyclops.evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;
import org.cyclops.evilcraft.tileentity.tickaction.EmptyItemBucketInTankTickAction;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.BulkRepairItemTickAction;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A machine that can infuse things with blood.
 * @author rubensworks
 *
 */
public class TileColossalBloodChest extends TileWorking<TileColossalBloodChest, MutableFloat> {

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
     * The name of the tank, used for NBT storage.
     */
    public static String TANKNAME = "colossalBloodChestTank";
    /**
     * The capacity of the tank.
     */
    public static final int LIQUID_PER_SLOT = FluidContainerRegistry.BUCKET_VOLUME * 10;
    /**
     * The amount of ticks per mB the tank can accept per tick.
     */
    public static final int TICKS_PER_LIQUID = 2;
    /**
     * The fluid that is accepted in the tank.
     */
    public static final Fluid ACCEPTED_FLUID = Blood.getInstance();

    public static final int MAX_EFFICIENCY = 200;

    @NBTPersist
    private Vec3i size = LocationHelpers.copyLocation(Vec3i.NULL_VECTOR);
    @NBTPersist
    private Vec3i renderOffset = new Vec3i(0, 0, 0);
    @Getter
    @Setter
    @NBTPersist
    private Integer efficiency = 0;
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

    private Block block = ColossalBloodChest.getInstance();

    public static final Upgrades.UpgradeEventType UPGRADEEVENT_SPEED = Upgrades.newUpgradeEventType();
    public static final Upgrades.UpgradeEventType UPGRADEEVENT_BLOODUSAGE = Upgrades.newUpgradeEventType();

    private static final Map<Class<?>, ITickAction<TileColossalBloodChest>> REPAIR_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileColossalBloodChest>>();
    static {
        REPAIR_TICK_ACTIONS.put(Item.class, new BulkRepairItemTickAction());
    }

    private static final Map<Class<?>, ITickAction<TileColossalBloodChest>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileColossalBloodChest>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(IFluidContainerItem.class, new EmptyFluidContainerInTankTickAction<TileColossalBloodChest>());
        EMPTY_IN_TANK_TICK_ACTIONS.put(Item.class, new EmptyItemBucketInTankTickAction<TileColossalBloodChest>());
    }

    /**
     * The multiblock structure detector for this furnace.
     */
    @SuppressWarnings("unchecked")
    public static CubeDetector detector = new HollowCubeDetector(
            new AllowedBlock[]{
                    new AllowedBlock(ReinforcedUndeadPlank.getInstance()),
                    new AllowedBlock(ColossalBloodChest.getInstance()).setExactOccurences(1)
            },
            Lists.newArrayList(ColossalBloodChest.getInstance(), ReinforcedUndeadPlank.getInstance())
    ).setExactSize(new Vec3i(2, 2, 2));

    /**
     * Make a new instance.
     */
    public TileColossalBloodChest() {
        super(
                SLOTS,
                ColossalBloodChest.getInstance().getLocalizedName(),
                LIQUID_PER_SLOT,
                TileColossalBloodChest.TANKNAME,
                ACCEPTED_FLUID);
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
                        >(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER, false)
        );

        // The slots side mapping
        List<Integer> inSlotsTank = new LinkedList<Integer>();
        inSlotsTank.add(SLOT_CONTAINER);
        List<Integer> inSlotsInventory = new LinkedList<Integer>();
        for(int i = 0; i < SLOTS_CHEST; i++) {
            inSlotsInventory.add(i);
        }
        addSlotsToSide(EnumFacing.EAST, inSlotsTank);
        addSlotsToSide(EnumFacing.UP, inSlotsInventory);
        addSlotsToSide(EnumFacing.DOWN, inSlotsInventory);
        addSlotsToSide(EnumFacing.SOUTH, inSlotsInventory);
        addSlotsToSide(EnumFacing.WEST, inSlotsInventory);

        // Upgrade behaviour
        upgradeBehaviour.put(UPGRADE_EFFICIENCY, new UpgradeBehaviour<TileColossalBloodChest, MutableFloat>(2) {
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
        upgradeBehaviour.put(UPGRADE_SPEED, new UpgradeBehaviour<TileColossalBloodChest, MutableFloat>(1) {
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

    protected void resetSlotHistory() {
        for(int i = 0; i < getBasicInventorySize(); i++) {
            slotTickHistory.put(i, false);
        }
    }
    
    @Override
    protected SingleUseTank newTank(String tankName, int tankSize) {
    	return new ImplicitFluidConversionTank(tankName, tankSize, this, BloodFluidConverter.getInstance());
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if(slot == SLOT_CONTAINER)
            return SlotFluidContainer.checkIsItemValid(itemStack, getTank());
        else if(slot <= SLOTS_CHEST && slot >= 0)
            return SlotRepairable.checkIsItemValid(itemStack);
        return super.isItemValidForSlot(slot, itemStack);
    }

    @Override
    public boolean canConsume(ItemStack itemStack) {
        return SlotRepairable.checkIsItemValid(itemStack);
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
    public boolean canWork() {
        Vec3i size = getSize();
        return size.compareTo(TileColossalBloodChest.detector.getExactSize()) == 0;
    }

    /**
     * Check if the spirit furnace on the given location is valid and can start working.
     * @param world The world.
     * @param location The location.
     * @return If it is valid.
     */
    public static boolean canWork(World world, BlockPos location) {
        TileColossalBloodChest tile = TileHelpers.getSafeTile(world, location, TileColossalBloodChest.class);
        return tile != null && tile.canWork();
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
        if(worldObj != null && !this.worldObj.isRemote && this.worldObj.getWorldTime() % ColossalBloodChestConfig.ticksPerDamage == 0) {
            int oldEfficiency = efficiency;
            efficiency = Math.max(0, efficiency - ColossalBloodChestConfig.baseConcurrentItems);
            if(oldEfficiency != efficiency) {
                sendUpdate();
            }
        }
        // Resynchronize clients with the server state, the last condition makes sure
        // not all chests are synced at the same time.
        if(worldObj != null
                && !this.worldObj.isRemote
                && this.playersUsing != 0
                && WorldHelpers.efficientTick(worldObj, TICK_MODULUS, getPos().hashCode())) {
            this.playersUsing = 0;
            float range = 5.0F;
            @SuppressWarnings("unchecked")
            List<EntityPlayer> entities = this.worldObj.getEntitiesWithinAABB(
                    EntityPlayer.class,
                    new AxisAlignedBB(
                            getPos().subtract(new Vec3i(range, range, range)),
                            getPos().add(new Vec3i(1 + range, 1 + range, 1 + range))
                    )
            );

            for(EntityPlayer player : entities) {
                if (player.openContainer instanceof ContainerColossalBloodChest) {
                    ++this.playersUsing;
                }
            }

            worldObj.addBlockEvent(getPos(), block, 1, playersUsing);
        }

        prevLidAngle = lidAngle;
        float increaseAngle = 0.05F;
        if (playersUsing > 0 && lidAngle == 0.0F) {
            EvilCraft.proxy.playSound(
                    (double) getPos().getX() + 0.5D,
                    (double) getPos().getY() + 0.5D,
                    (double) getPos().getZ() + 0.5D,
                    SoundEvents.block_chest_open,
                    SoundCategory.BLOCKS,
                    0.5F,
                    worldObj.rand.nextFloat() * 0.1F + 0.5F
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
                EvilCraft.proxy.playSound(
                        (double) getPos().getX() + 0.5D,
                        (double) getPos().getY() + 0.5D,
                        (double) getPos().getZ() + 0.5D,
                        SoundEvents.block_chest_close,
                        SoundCategory.BLOCKS,
                        0.5F,
                        worldObj.rand.nextFloat() * 0.1F + 0.5F
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
    public void openInventory(EntityPlayer entityPlayer) {
        triggerPlayerUsageChange(1);
    }

    @Override
    public void closeInventory(EntityPlayer entityPlayer) {
        triggerPlayerUsageChange(-1);
    }

    private void triggerPlayerUsageChange(int change) {
        if (worldObj != null) {
            playersUsing += change;
            worldObj.addBlockEvent(getPos(), block, 1, playersUsing);
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return super.isUseableByPlayer(entityPlayer)
                && (worldObj == null || worldObj.getTileEntity(getPos()) != this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos().subtract(new Vec3i(3, 3, 3)), getPos().add(3, 6, 3));
    }

    public void setCenter(BlockPos center) {
        EnumFacing rotation = EnumFacing.NORTH;
        if(center.getX() != getPos().getX()) {
            rotation = DirectionHelpers.getEnumFacingFromXSign(center.getX() - getPos().getX());
        } else if(center.getZ() != getPos().getZ()) {
            rotation = DirectionHelpers.getEnumFacingFromZSing(center.getZ() - getPos().getZ());
        }
        this.setRotation(rotation);
        this.renderOffset = getPos().subtract(center);
    }

    public Vec3i getRenderOffset() {
        return this.renderOffset;
    }

    /**
     * Callback for when a structure has been detected for a spirit furnace block.
     * @param world The world.
     * @param location The location of one block of the structure.
     * @param size The size of the structure.
     * @param valid If the structure is being validated(/created), otherwise invalidated.
     * @param originCorner The origin corner
     */
    public static void detectStructure(World world, BlockPos location, Vec3i size, boolean valid, BlockPos originCorner) {

    }

}
