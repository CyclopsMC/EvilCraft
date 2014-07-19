package evilcraft.entities.tileentities;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.IFluidContainerItem;

import com.google.common.collect.Lists;

import evilcraft.Configs;
import evilcraft.api.Helpers;
import evilcraft.api.algorithms.ILocation;
import evilcraft.api.algorithms.Locations;
import evilcraft.api.algorithms.Size;
import evilcraft.api.block.AllowedBlock;
import evilcraft.api.block.CubeDetector;
import evilcraft.api.block.HollowCubeDetector;
import evilcraft.api.entities.tileentitites.NBTPersist;
import evilcraft.api.entities.tileentitites.WorkingTileEntity;
import evilcraft.api.entities.tileentitites.tickaction.ITickAction;
import evilcraft.api.entities.tileentitites.tickaction.TickComponent;
import evilcraft.api.gui.slot.SlotFluidContainer;
import evilcraft.api.world.FakeWorldItemDelegator.IItemDropListener;
import evilcraft.blocks.BoxOfEternalClosure;
import evilcraft.blocks.BoxOfEternalClosureConfig;
import evilcraft.blocks.DarkBloodBrick;
import evilcraft.blocks.SpiritFurnace;
import evilcraft.entities.tileentities.tickaction.EmptyFluidContainerInTankTickAction;
import evilcraft.entities.tileentities.tickaction.EmptyItemBucketInTankTickAction;
import evilcraft.entities.tileentities.tickaction.spiritfurnace.BoxCookTickAction;
import evilcraft.fluids.Blood;
import evilcraft.network.PacketHandler;
import evilcraft.network.packets.DetectionListenerPacket;

/**
 * A furnace that is able to cook spirits for their inner entity drops.
 * @author rubensworks
 *
 */
public class TileSpiritFurnace extends WorkingTileEntity<TileSpiritFurnace> implements IItemDropListener {
    
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
    public static final int[] SLOTS_DROP = new int[]{2, 3, 4, 5};
    
    /**
     * The total amount of slots in this machine.
     */
    public static final int SLOTS = 2 + SLOTS_DROP.length;
    
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
    	BOX_COOK_TICK_ACTIONS.put(BoxOfEternalClosure.class, new BoxCookTickAction());
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
        for(int slot : SLOTS_DROP) {
        	outSlots.add(slot);
        }
        addSlotsToSide(ForgeDirection.EAST, inSlotsTank);
        addSlotsToSide(ForgeDirection.UP, inSlots);
        addSlotsToSide(ForgeDirection.DOWN, outSlots);
        addSlotsToSide(ForgeDirection.SOUTH, outSlots);
        addSlotsToSide(ForgeDirection.WEST, outSlots);
    }
    
    @Override
	protected int getWorkTicker() {
		return cookTicker;
	}
    
    @Override
    public boolean canWork() {
    	Size size = getSize();
		return size.compareTo(TileSpiritFurnace.detector.getMinimumSize()) >= 0;
    }
    
    /**
     * Check if the spirit furnace on the given location is valid and can start working.
     * @param world The world.
     * @param location The location.
     * @return If it is valid.
     */
    public static boolean canWork(World world, ILocation location) {
    	TileEntity tile = Locations.getTile(world, location);
		if(tile != null) {
			return ((TileSpiritFurnace) tile).canWork();
		}
		return false;
    }
    
    /**
     * Get the allowed cooking item for this furnace.
     * @return The allowed item.
     */
    public static Item getAllowedCookItem() {
    	Item allowedItem = Items.apple;
        if(Configs.isEnabled(BoxOfEternalClosureConfig.class)) {
        	allowedItem = Item.getItemFromBlock(BoxOfEternalClosure.getInstance());
        }
        return allowedItem;
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
    
    @Override
    public boolean canConsume(ItemStack itemStack) {
        return itemStack != null && getAllowedCookItem() == itemStack.getItem();
    }
    
    /**
     * Get the id of the infusion slot.
     * @return id of the infusion slot.
     */
    public int getConsumeSlot() {
        return SLOT_BOX;
    }

    /**
     * Get the ids of the result slots.
     * @return ids of the result slots.
     */
    public int[] getProduceSlots() {
        return SLOTS_DROP;
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
	public void onItemDrop(ItemStack itemStack) {
		boolean placed = false;
		int[] slots = getProduceSlots();
		int i = 0;
		while(!placed && i < slots.length) {
			ItemStack produceStack = getInventory().getStackInSlot(slots[i]);
	        if(produceStack == null) {
	            getInventory().setInventorySlotContents(slots[i], itemStack);
	            placed = true;
	        } else {
	            if(produceStack.getItem() == itemStack.getItem()
	               && produceStack.getMaxStackSize() >= produceStack.stackSize + itemStack.stackSize) {
	                produceStack.stackSize += itemStack.stackSize;
	                placed = true;
	            }
	        }
	        i++;
		}
	}

}
