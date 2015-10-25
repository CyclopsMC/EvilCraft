package evilcraft.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.ILocation;
import evilcraft.block.ColossalBloodChest;
import evilcraft.block.ColossalBloodChestConfig;
import evilcraft.block.ReinforcedUndeadPlank;
import evilcraft.core.algorithm.Location;
import evilcraft.core.algorithm.Size;
import evilcraft.core.block.AllowedBlock;
import evilcraft.core.block.CubeDetector;
import evilcraft.core.block.HollowCubeDetector;
import evilcraft.core.fluid.BloodFluidConverter;
import evilcraft.core.fluid.ImplicitFluidConversionTank;
import evilcraft.core.fluid.SingleUseTank;
import evilcraft.core.helper.DirectionHelpers;
import evilcraft.core.helper.LocationHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.helper.WorldHelpers;
import evilcraft.core.inventory.slot.SlotFluidContainer;
import evilcraft.core.tileentity.NBTPersist;
import evilcraft.core.tileentity.tickaction.ITickAction;
import evilcraft.core.tileentity.tickaction.TickComponent;
import evilcraft.core.tileentity.upgrade.IUpgradeSensitiveEvent;
import evilcraft.core.tileentity.upgrade.UpgradeBehaviour;
import evilcraft.core.tileentity.upgrade.Upgrades;
import evilcraft.fluid.Blood;
import evilcraft.inventory.container.ContainerColossalBloodChest;
import evilcraft.inventory.slot.SlotRepairable;
import evilcraft.tileentity.tickaction.EmptyFluidContainerInTankTickAction;
import evilcraft.tileentity.tickaction.EmptyItemBucketInTankTickAction;
import evilcraft.tileentity.tickaction.bloodchest.BulkRepairItemTickAction;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.apache.commons.lang3.mutable.MutableFloat;

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
    private Size size = Size.NULL_SIZE.copy();
    @NBTPersist
    private ILocation renderOffset = Size.NULL_SIZE.copy();
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
    ).setExactSize(new Size(2, 2, 2));

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
        addSlotsToSide(ForgeDirection.EAST, inSlotsTank);
        addSlotsToSide(ForgeDirection.UP, inSlotsInventory);
        addSlotsToSide(ForgeDirection.DOWN, inSlotsInventory);
        addSlotsToSide(ForgeDirection.SOUTH, inSlotsInventory);
        addSlotsToSide(ForgeDirection.WEST, inSlotsInventory);

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
        return false;
    }

    @Override
    public boolean canConsume(ItemStack itemStack) {
        return SlotRepairable.checkIsItemValid(itemStack);
    }

    /**
     * @return the size
     */
    public Size getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(Size size) {
        this.size = size;
        sendUpdate();
    }

    @Override
    public boolean canWork() {
        Size size = getSize();
        return size.compareTo(TileColossalBloodChest.detector.getExactSize()) == 0;
    }

    /**
     * Check if the spirit furnace on the given location is valid and can start working.
     * @param world The world.
     * @param location The location.
     * @return If it is valid.
     */
    public static boolean canWork(World world, ILocation location) {
        TileEntity tile = LocationHelpers.getTile(world, location);
        if(tile != null) {
            return ((TileColossalBloodChest) tile).canWork();
        }
        return false;
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
                && WorldHelpers.efficientTick(worldObj, TICK_MODULUS, this.xCoord, this.yCoord, this.zCoord)/*(this.ticksSinceSync + this.xCoord + this.yCoord + this.zCoord) % 200 == 0*/) {
            this.playersUsing = 0;
            float range = 5.0F;
            @SuppressWarnings("unchecked")
            List<EntityPlayer> entities = this.worldObj.getEntitiesWithinAABB(
                    EntityPlayer.class,
                    AxisAlignedBB.getBoundingBox(
                            (double) ((float) this.xCoord - range),
                            (double) ((float) this.yCoord - range),
                            (double) ((float) this.zCoord - range),
                            (double) ((float) (this.xCoord + 1) + range),
                            (double) ((float) (this.yCoord + 1) + range),
                            (double) ((float) (this.zCoord + 1) + range)
                    )
            );

            for(EntityPlayer player : entities) {
                if (player.openContainer instanceof ContainerColossalBloodChest) {
                    ++this.playersUsing;
                }
            }

            worldObj.addBlockEvent(xCoord, yCoord, zCoord, block, 1, playersUsing);
        }

        prevLidAngle = lidAngle;
        float increaseAngle = 0.05F;
        if (playersUsing > 0 && lidAngle == 0.0F) {
            worldObj.playSoundEffect(
                    (double) xCoord + 0.5D,
                    (double) yCoord + 0.5D,
                    (double) zCoord + 0.5D,
                    "random.chestopen",
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
                worldObj.playSoundEffect(
                        (double) xCoord + 0.5D,
                        (double) yCoord + 0.5D,
                        (double) zCoord + 0.5D,
                        "random.chestclosed",
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
    public void openInventory() {
        triggerPlayerUsageChange(1);
    }

    @Override
    public void closeInventory() {
        triggerPlayerUsageChange(-1);
    }

    private void triggerPlayerUsageChange(int change) {
        if (worldObj != null) {
            playersUsing += change;
            worldObj.addBlockEvent(xCoord, yCoord, zCoord, block, 1, playersUsing);
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return super.isUseableByPlayer(entityPlayer)
                && (worldObj == null || worldObj.getTileEntity(xCoord, yCoord, zCoord) != this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord - 3, yCoord - 3, zCoord - 3, xCoord + 3, yCoord + 6, zCoord + 3);
    }

    public void setCenter(ILocation center) {
        ForgeDirection rotation = ForgeDirection.NORTH;
        int[] c = center.getCoordinates();
        if(c[0] != this.xCoord) {
            rotation = DirectionHelpers.getForgeDirectionFromXSign(c[0] - this.xCoord);
        } else if(c[2] != this.zCoord) {
            rotation = DirectionHelpers.getForgeDirectionFromZSing(c[2] - this.zCoord);
        }
        this.setRotation(rotation);
        this.renderOffset = new Location(this.xCoord, this.yCoord, this.zCoord).subtract(center);
    }

    public ILocation getRenderOffset() {
        return this.renderOffset;
    }

    /**
     * Callback for when a structure has been detected for a spirit furnace block.
     * @param world The world.
     * @param location The location of one block of the structure.
     * @param size The size of the structure.
     * @param valid If the structure is being validated(/created), otherwise invalidated.
     */
    public static void detectStructure(World world, ILocation location, Size size, boolean valid) {
        int newMeta = valid ? 1 : 0;
        boolean change = LocationHelpers.getBlockMeta(world, location) != newMeta;
        LocationHelpers.setBlockMetadata(world, location, newMeta, MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
    }

}
