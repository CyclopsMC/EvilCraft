package evilcraft.entities.tileentities;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;

import com.google.common.collect.Lists;

import evilcraft.api.Helpers;
import evilcraft.api.algorithms.ILocation;
import evilcraft.api.algorithms.Locations;
import evilcraft.api.algorithms.Size;
import evilcraft.api.block.AllowedBlock;
import evilcraft.api.block.CubeDetector;
import evilcraft.api.block.HollowCubeDetector;
import evilcraft.api.entities.tileentitites.TickingTankInventoryTileEntity;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.api.entities.tileentitites.tickaction.TickComponent;
import evilcraft.blocks.DarkBloodBrick;
import evilcraft.blocks.SpiritFurnace;
import evilcraft.entities.tileentities.tickaction.EmptyFluidContainerInTankTickAction;
import evilcraft.entities.tileentities.tickaction.EmptyItemBucketInTankTickAction;
import evilcraft.entities.tileentities.tickaction.spiritfurnace.BoxCookTickAction;
import evilcraft.fluids.Blood;
import evilcraft.gui.slot.SlotFluidContainer;
import evilcraft.network.PacketHandler;
import evilcraft.network.packets.DetectionListenerPacket;

/**
 * A furnace that is able to cook spirits for their inner entity drops.
 * @author rubensworks
 *
 */
public class TileSpiritFurnace extends TickingTankInventoryTileEntity<TileSpiritFurnace> {
    
    /**
     * The total amount of slots in this machine.
     */
    public static final int SLOTS = 3;
    /**
     * The id of the fluid container drainer slot.
     */
    public static final int SLOT_CONTAINER = 0;
    /**
     * The id of the box slot.
     */
    public static final int SLOT_BOX = 1;
    /**
     * The id of the drops slot.
     */
    public static final int SLOT_DROP = 2;
    
    /**
     * The name of the tank, used for NBT storage.
     */
    public static String TANKNAME = "spiritFurnaceTank";
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
    
    /**
     * The multiblock structure detector for this furnace.
     */
    @SuppressWarnings("unchecked")
	public static CubeDetector detector = new HollowCubeDetector(
    			new AllowedBlock[]{
    					new AllowedBlock(DarkBloodBrick.getInstance()),
    					new AllowedBlock(SpiritFurnace.getInstance()).setMaxOccurences(1)
    					},
    			Lists.newArrayList(SpiritFurnace.getInstance(), DarkBloodBrick.getInstance())
    		).setMinimumSize(new Size(new int[]{2, 2, 2}));
    
    private int cookTicker;
    
    private static final Map<Class<?>, ITickAction<TileSpiritFurnace>> BOX_COOK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileSpiritFurnace>>();
    static {
    	BOX_COOK_TICK_ACTIONS.put(ItemBucket.class, new BoxCookTickAction());
    }
    
    private static final Map<Class<?>, ITickAction<TileSpiritFurnace>> EMPTY_IN_TANK_TICK_ACTIONS = new LinkedHashMap<Class<?>, ITickAction<TileSpiritFurnace>>();
    static {
        EMPTY_IN_TANK_TICK_ACTIONS.put(ItemBucket.class, new EmptyItemBucketInTankTickAction<TileSpiritFurnace>());
        EMPTY_IN_TANK_TICK_ACTIONS.put(IFluidContainerItem.class, new EmptyFluidContainerInTankTickAction<TileSpiritFurnace>());
    }
    
    @NBTPersist
    private Size size = Size.NULL_SIZE.copy();
    
    /**
     * Make a new instance.
     */
    public TileSpiritFurnace() {
        super(
                SLOTS,
                SpiritFurnace.getInstance().getLocalizedName(),
                LIQUID_PER_SLOT,
                TileSpiritFurnace.TANKNAME,
                ACCEPTED_FLUID);
        cookTicker = addTicker(
                new TickComponent<
                    TileSpiritFurnace,
                    ITickAction<TileSpiritFurnace>
                >(this, BOX_COOK_TICK_ACTIONS, SLOT_BOX)
                );
        addTicker(
                new TickComponent<
                    TileSpiritFurnace,
                    ITickAction<TileSpiritFurnace>
                >(this, EMPTY_IN_TANK_TICK_ACTIONS, SLOT_CONTAINER)
                );
        
        // The slots side mapping
        List<Integer> inSlots = new LinkedList<Integer>();
        inSlots.add(SLOT_BOX);
        List<Integer> inSlotsTank = new LinkedList<Integer>();
        inSlotsTank.add(SLOT_CONTAINER);
        List<Integer> outSlots = new LinkedList<Integer>();
        outSlots.add(SLOT_DROP);
        addSlotsToSide(ForgeDirection.EAST, inSlotsTank);
        addSlotsToSide(ForgeDirection.UP, inSlots);
        addSlotsToSide(ForgeDirection.DOWN, outSlots);
        addSlotsToSide(ForgeDirection.SOUTH, outSlots);
        addSlotsToSide(ForgeDirection.WEST, outSlots);
    }
    
    /**
     * Check if the spirit furnace on the given location is valid and can start working.
     * @param world The world.
     * @param location The location.
     * @return If it is valid.
     */
    public static boolean isValid(World world, ILocation location) {
    	TileEntity tile = Locations.getTile(world, location);
		if(tile != null) {
			Size size = ((TileSpiritFurnace) tile).getSize();
			return size.compareTo(TileSpiritFurnace.detector.getMinimumSize()) >= 0;
		}
		return false;
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
		boolean change = Locations.getBlockMeta(world, location) != newMeta;
		Locations.setBlockMetadata(world, location, newMeta, Helpers.BLOCK_NOTIFY_CLIENT);
		if(change) {
			PacketHandler.sendToServer(new DetectionListenerPacket(location, valid));
		}
    }
    
    /**
     * Check if the given item can be infused.
     * @param itemStack The item to check.
     * @return If it can be infused.
     */
    public boolean canConsume(ItemStack itemStack) {
        // TODO
        
        // In all other cases: false
        return false;
    }
    
    /**
     * Get the id of the infusion slot.
     * @return id of the infusion slot.
     */
    public int getConsumeSlot() {
        return SLOT_BOX;
    }

    /**
     * Get the id of the result slot.
     * @return id of the result slot.
     */
    public int getProduceSlot() {
        return SLOT_DROP;
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if(slot == SLOT_BOX)
            return canConsume(itemStack);
        if(slot == SLOT_CONTAINER)
            return SlotFluidContainer.checkIsItemValid(itemStack, ACCEPTED_FLUID);
        return false;
    }
    
    /**
     * If this tile is cooking.
     * @return If it is infusing.
     */
    public boolean isCooking() {
        return getCookTick() > 0;
    }
    
    /**
     * If the furnace should visually (block icon) show it is cooking, should only be called client-side.
     * @return If the state is cooking.
     */
    public boolean isBlockCooking() {
        return getCurrentState() == 1;
    }

    /**
     * Get the cooking progress scaled, to be used in GUI's.
     * @param scale The scale this progress should be applied to.
     * @return The scaled cooking progress.
     */
    public int getCookTickScaled(int scale) {
        return (int) ((float)getCookTick() / (float)getRequiredTicks() * (float)scale);
    }
    
    private int getCookTick() {
        return getTickers().get(cookTicker).getTick();
    }
    
    private int getRequiredTicks() {
        return getTickers().get(cookTicker).getRequiredTicks();
    }
    
    /**
     * Resets the ticks of the infusion.
     */
    public void resetInfusion() {
        getTickers().get(cookTicker).setTick(0);
        getTickers().get(cookTicker).setRequiredTicks(0);
    }

    @Override
    public int getNewState() {
        return this.isCooking()?1:0;
    }
    

    @Override
    public void onStateChanged() {
        sendUpdate();
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

}
