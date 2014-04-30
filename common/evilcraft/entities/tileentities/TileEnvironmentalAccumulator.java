package evilcraft.entities.tileentities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import org.lwjgl.util.vector.Vector4f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Coordinate;
import evilcraft.api.Helpers;
import evilcraft.api.degradation.DegradationExecutor;
import evilcraft.api.degradation.IDegradable;
import evilcraft.blocks.EnvironmentalAccumulator;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;
import evilcraft.items.WeatherContainer;
import evilcraft.items.WeatherContainer.WeatherContainerTypes;

/**
 * Machine that can accumulate the weather and put it in a bottle.
 * @author immortaleeb
 *
 */
public class TileEnvironmentalAccumulator extends EvilCraftBeaconTileEntity implements IBossDisplayData, IDegradable {
    
    private static final int ITEM_MOVE_DURATION = 100;
    private static final int ITEM_MOVE_COOLDOWN_DURATION = 100;
    private static final float ITEM_MOVE_SPEED = 0.3f / 20;
    
    private static final double WEATHER_CONTAINER_MIN_DROP_HEIGHT = 0.0;
    private static final double WEATHER_CONTAINER_MAX_DROP_HEIGHT = 2.0;
    private static final double WEATHER_CONTAINER_SPAWN_HEIGHT = ITEM_MOVE_DURATION * ITEM_MOVE_SPEED + 1;
    
    private static final int DEGRADATION_RADIUS_BASE = 5;
    private static final int DEGRADATION_TICK_INTERVAL = 100;
    
    private DegradationExecutor degradationExecutor;
    // This number rises with the number of uses of the env. accum.
    private int degradation = 0;
    
    /**
     * Holds the state of the environmental accumulator.
     * The following states are possible: idle (the default case), cooling down,
     * processing an item and dropping an item. The different states can be found as
     * public static variables of {@link EnvironmentalAccumulator}.
     */
    private int state = 0;
    private int tick = 0;
    
    @SideOnly(Side.CLIENT)
    private float movingItemY;
    
    /**
     * Make a new instance.
     */
	public TileEnvironmentalAccumulator() {
	    super();
	    
	    degradationExecutor = new DegradationExecutor(this);
	    degradationExecutor.setTickInterval(DEGRADATION_TICK_INTERVAL);
	    
	    if (Helpers.isClientSide()) {
	        setBeamInnerColor(getInnerColorByState(state));
	        setBeamOuterColor(getOuterColorByState(state));
	        
	        movingItemY = -1.0f;
	    }
	}
	
	@SideOnly(Side.CLIENT)
	private Vector4f getInnerColorByState(int state) {
	    if (state == EnvironmentalAccumulator.STATE_PROCESSING_ITEM)
	        return new Vector4f(0.48046875F, 0.29296875F, 0.1171875F, 0.05f);
	    if (state == EnvironmentalAccumulator.STATE_IDLE)
	        return new Vector4f(0.48046875F, 0.29296875F, 0.1171875F, 0.13f);
	    else
	        return new Vector4f(0, 0, 0, 0.13f);
	        
	}
	
	@SideOnly(Side.CLIENT)
    private Vector4f getOuterColorByState(int state) {
        if (state == EnvironmentalAccumulator.STATE_COOLING_DOWN)
            return new Vector4f(0, 0, 0, 0.13f);
        else
            return new Vector4f(0.30078125F, 0.1875F, 0.08203125F, 0.13f);
    }
	
	/**
	 * Get the maximum cooldown tick for accumulating weather.
	 * @return The maximum cooldown tick.
	 */
	public int getMaxCooldownTick() {
	    return EnvironmentalAccumulatorConfig.tickCooldown;
	}
	
	/**
	 * Get the Y coordinate of the current moving item.
	 * @return The Y coordinate of the inner item.
	 */
	@SideOnly(Side.CLIENT)
	public float getMovingItemY() {
	    return movingItemY;
	}
	
	@Override
	public void updateEntity() {
	    // Keep ticking if necessary
	    if (tick > 0)
	        tick--;
	    
	    if (state == EnvironmentalAccumulator.STATE_IDLE) {
            updateEnvironmentalAccumulatorIdle();
        } // Are we processing an item?
        else if (state == EnvironmentalAccumulator.STATE_PROCESSING_ITEM) {
            // Are we done moving the item?
            if (tick == 0) {
                dropWeatherContainer();
                activateFinishedProcessingItemState();
            }
        } // Have we just finished processing an item?
        else if (state == EnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM) {
            // We stay in this state for a while so the client gets some time to 
            // show the corresponding effect when an item is finished processing
            
            // Are we done waiting for the client to update?
            if (tick == 0) {
                activateCooldownState();
            }
        } // Are we cooling down?
        else if (state == EnvironmentalAccumulator.STATE_COOLING_DOWN) {
	        // TODO: in the rewrite of this tile entity, it should be ensured that the
	        // random effect is equal on client and server side?
	        degradationExecutor.runRandomEffect(worldObj.isRemote);
	        
	        // Are we done cooling down?
	        if (tick == 0)
	            activateIdleState();
	    }
	    
	    // Some extra stuff needs to be update on the client side
	    if (worldObj.isRemote)
	        updateClient();
	}
	
	private void updateEnvironmentalAccumulatorIdle() {
        // Look for items thrown into the beam
        @SuppressWarnings("rawtypes")
        List containers = worldObj.getEntitiesWithinAABB(EntityItem.class, 
                AxisAlignedBB.getBoundingBox(
                        this.xCoord, this.yCoord + WEATHER_CONTAINER_MIN_DROP_HEIGHT, this.zCoord, 
                        this.xCoord + 1.0, this.yCoord + WEATHER_CONTAINER_MAX_DROP_HEIGHT, this.zCoord + 1.0)
                );
        
        int i = 0;
        boolean foundEmptyContainer = false;
        
        while (i < containers.size() && !foundEmptyContainer) {
            EntityItem container = (EntityItem)containers.get(i);
            ItemStack stack = container.getEntityItem();
            
            // Does the stack contain empty weather containers?
            if (stack.itemID == WeatherContainer.getInstance().itemID && WeatherContainer.isEmpty(stack.getItemDamage())) {
                foundEmptyContainer = true;
                
                if (!worldObj.isRemote)
                    decreaseStackSize(container, stack);
                
                activateProcessingItemState();
            }
            
            i++;
        }
	}
	
	private void updateClient() {
	    // TODO: make item move speed dependend on the item thrown in 
	    if (state == EnvironmentalAccumulator.STATE_PROCESSING_ITEM)
	        movingItemY += ITEM_MOVE_SPEED;
	}
	
	private void decreaseStackSize(EntityItem container, ItemStack stack) {
	    stack.stackSize--;
	    
	    if (stack.stackSize == 0)
	        container.setDead();
	}
	
	private void dropWeatherContainer() {
	    if (!worldObj.isRemote) {
    	    // Create empty container and fill it with the current weather
    	    ItemStack itemStack = WeatherContainer.createItemStack(WeatherContainerTypes.EMPTY, 1);
    	    ((WeatherContainer)itemStack.getItem()).onFill(worldObj, itemStack);
    	    
    	    // Drop the weather container on the ground
    	    EntityItem entity = new EntityItem(worldObj, this.xCoord, this.yCoord + WEATHER_CONTAINER_SPAWN_HEIGHT, this.zCoord);
    	    entity.setEntityItemStack(itemStack);
    	    
    	    worldObj.spawnEntityInWorld(entity);
	    }
	}
	
	private void activateIdleState() {
        tick = 0;
        state = EnvironmentalAccumulator.STATE_IDLE;
        
        if (!worldObj.isRemote)
            sendUpdate();
    }
	
	private void activateProcessingItemState() {
	    // TODO: make this variable depending on the item thrown in...
	    tick = ITEM_MOVE_DURATION;
	    state = EnvironmentalAccumulator.STATE_PROCESSING_ITEM;
	    
	    if (!worldObj.isRemote)
	        sendUpdate();
	}
	
	private void activateFinishedProcessingItemState() {
	    tick = ITEM_MOVE_COOLDOWN_DURATION;
	    state = EnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM;
	    
	    if (!worldObj.isRemote)
	        sendUpdate();
	}
	
	private void activateCooldownState() {
	    degradation++;
	    // TODO: make this variable depending on the item thrown in...
	    tick = getMaxCooldownTick();
	    state = EnvironmentalAccumulator.STATE_COOLING_DOWN;
	    
	    if (!worldObj.isRemote)
	        sendUpdate();
	}
	
	@Override
	public void onUpdateReceived() {
	    // If we receive an update from the server and our new state is the
	    // finished processing item state, show the corresponding effect
	    if (worldObj.isRemote && state == EnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM) {
	        // TODO: make this effect dependend on the item that was processed
	        this.worldObj.playAuxSFX(2002, (int)Math.round(xCoord), (int)Math.round(yCoord + WEATHER_CONTAINER_SPAWN_HEIGHT), (int)Math.round(zCoord), 16428);
	        movingItemY = -1.0f;
	    }
	    
	    // Change the beam colors if we receive an update
	    setBeamColors(state);
	}
	
	/**
	 * Set the beam colors.
	 * @param state The state to base the colors on.
	 */
	public void setBeamColors(int state) {
        if (worldObj.isRemote) { 
    	    setBeamInnerColor(getInnerColorByState(state));
    	    setBeamOuterColor(getOuterColorByState(state));
        }
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
	    super.readFromNBT(compound);
	    
	    degradation = compound.getInteger("degradation");
	    tick = compound.getInteger("tick");
	    state = compound.getInteger("state");
	    
	    degradationExecutor.readFromNBT(compound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
	    super.writeToNBT(compound);
	    
	    compound.setInteger("degradation", degradation);
	    compound.setInteger("tick", tick);
	    compound.setInteger("state", state);
	    
	    degradationExecutor.writeToNBT(compound);
	}
    
    @Override
    public float getMaxHealth() {
        if (state == EnvironmentalAccumulator.STATE_PROCESSING_ITEM)
            return ITEM_MOVE_DURATION;
        
        if (state == EnvironmentalAccumulator.STATE_FINISHED_PROCESSING_ITEM)
            return 0;
        
        return getMaxCooldownTick();
    }

    @Override
    public float getHealth() {
        if (state == EnvironmentalAccumulator.STATE_PROCESSING_ITEM)
            return ITEM_MOVE_DURATION - tick;
        
        if (state == EnvironmentalAccumulator.STATE_COOLING_DOWN)
            return getMaxCooldownTick() - tick;
        
        return getMaxCooldownTick();
    }

    @Override
    public String getEntityName() {
        return EnvironmentalAccumulatorConfig._instance.NAME + " Charge";
    }

    @Override
    public Coordinate getLocation() {
        return new Coordinate(xCoord, yCoord, zCoord);
    }

    @Override
    public int getRadius() {
        return DEGRADATION_RADIUS_BASE + degradation / 10;
    }

    @Override
    public List<Entity> getAreaEntities() {
        return Helpers.getEntitiesInArea(getWorld(), xCoord, yCoord, zCoord, getRadius());
    }

    @Override
    public double getDegradation() {
        return this.degradation;
    }

    @Override
    public World getWorld() {
        return this.worldObj;
    }
}
