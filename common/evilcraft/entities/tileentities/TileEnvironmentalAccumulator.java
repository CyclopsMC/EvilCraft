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
    private int degradation = 0;
    
    private int cooldownTick = 0;
    private boolean cooldown = false;
    
    private int moveItemTick = 0;
    private int moveItemCooldownTick = 0;
    private boolean movingItem = false;
    
    private int lastMetadata;
    
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
	        setBeamInnerColor(getInnerColorByMetadata(EnvironmentalAccumulator.BEAM_INACTIVE));
	        setBeamOuterColor(getOuterColorByMetadata(EnvironmentalAccumulator.BEAM_INACTIVE));
	        
	        movingItemY = -1.0f;
	        lastMetadata = -1;
	    }
	}
	
	@SideOnly(Side.CLIENT)
	private Vector4f getInnerColorByMetadata(int metadata) {
	    if (EnvironmentalAccumulator.isMovingItem(metadata) && EnvironmentalAccumulator.isBeamActive(metadata))
	        return new Vector4f(0.48046875F, 0.29296875F, 0.1171875F, 0.05f);
	    if (EnvironmentalAccumulator.isBeamActive(metadata))
	        return new Vector4f(0.48046875F, 0.29296875F, 0.1171875F, 0.13f);
	    else
	        return new Vector4f(0, 0, 0, 0.13f);
	        
	}
	
	@SideOnly(Side.CLIENT)
    private Vector4f getOuterColorByMetadata(int metadata) {
        if (!EnvironmentalAccumulator.isBeamActive(metadata))
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
	    if(cooldownTick > 0)
	        cooldownTick--;
	    
	    if (!worldObj.isRemote) {
	        moveItemTick--;
            
	        if (cooldown && cooldownTick <= 0)
	            deactivateCooldown();
	        
	        if (movingItem && moveItemTick <= 0) {
                deactivateMoveItem();
                dropWeatherContainer();
                activateCooldown();
            }
	        
	        if (cooldown)
	            updateDoneItemMoving();
	        
	        if (!(cooldown || movingItem))
	            updateEnvironmentalAccumulator();
	    } else {
	        updateClient();
	    }
	    
	    // TODO: in the rewrite of this tile entity, it should be ensured that the
	    // random effect is equal on client and server side?
	    if(cooldownTick > 0) {
	        degradationExecutor.runRandomEffect(worldObj.isRemote);
	    }
	}
	
	private void updateEnvironmentalAccumulator() {
	    if (!worldObj.isRemote) {
	        
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
                    decreaseStackSize(container, stack);
                    activateMoveItem();
                }
                
                i++;
            }
	    }
	}
	
	private void updateClient() {
	    int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	    
	    // We use the metadata to indicate an update from the server
	    // So here we check if the server has sent us an update
	    if (metadata == lastMetadata) {
	        if (EnvironmentalAccumulator.isMovingItem(metadata) && !EnvironmentalAccumulator.isDoneMovingItem(metadata)) {
	            movingItemY += ITEM_MOVE_SPEED;
	        }
	    } else {
	        if (EnvironmentalAccumulator.isDoneMovingItem(metadata)) {
	         // Stop showing the moving item animation
                // TODO: make a custom particle effect for this
                this.worldObj.playAuxSFX(2002, (int)Math.round(xCoord), (int)Math.round(yCoord + WEATHER_CONTAINER_SPAWN_HEIGHT), (int)Math.round(zCoord), 16428);
                movingItemY = -1.0f;
	        } else if (EnvironmentalAccumulator.isMovingItem(metadata)) {
    	        movingItemY = 0f;
    	    }
    	    
    	    setBeamColors(metadata);
    	    
    	    this.lastMetadata = metadata;
	    }
	}
	
	private void decreaseStackSize(EntityItem container, ItemStack stack) {
	    stack.stackSize--;
	    
	    if (stack.stackSize == 0)
	        container.setDead();
	}
	
	private void dropWeatherContainer() {
	    // Create empty container and fill it with the current weather
	    ItemStack itemStack = WeatherContainer.createItemStack(WeatherContainerTypes.EMPTY, 1);
	    ((WeatherContainer)itemStack.getItem()).onFill(worldObj, itemStack);
	    
	    // Drop the weather container on the ground
	    EntityItem entity = new EntityItem(worldObj, this.xCoord, this.yCoord + WEATHER_CONTAINER_SPAWN_HEIGHT, this.zCoord);
	    entity.setEntityItemStack(itemStack);
	    
	    worldObj.spawnEntityInWorld(entity);
	}
	
	private void activateMoveItem() {
        moveItemTick = ITEM_MOVE_DURATION;
        movingItem = true;
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, EnvironmentalAccumulator.MOVING_ITEM, 2);
    }
    
    private void deactivateMoveItem() {
        moveItemCooldownTick = ITEM_MOVE_COOLDOWN_DURATION;
        movingItem = false;
        moveItemTick = 0;
    }
	
	private void activateCooldown() {
	    degradation++;
	    cooldownTick = getMaxCooldownTick();
	    cooldown = true;
	    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, EnvironmentalAccumulator.BEAM_INACTIVE | EnvironmentalAccumulator.MOVING_ITEM, 2);
	    sendUpdate();
	}
	
	private void deactivateCooldown() {
	    cooldownTick = 0;
	    cooldown = false;
	    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, EnvironmentalAccumulator.BEAM_ACTIVE, 2);
	    sendUpdate();
	}
	
	/**
	 * Set the beam colors.
	 * @param metadata The metadata to base the colors on.
	 */
	public void setBeamColors(int metadata) {
        if (worldObj.isRemote) { 
    	    setBeamInnerColor(getInnerColorByMetadata(metadata));
    	    setBeamOuterColor(getOuterColorByMetadata(metadata));
        }
	}
	
	/**
	 * Called when the machine is done putting something in a bottle.
	 */
	public void updateDoneItemMoving() {
	    int metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	    
	    if (EnvironmentalAccumulator.isDoneMovingItem(metadata)) { 
	        moveItemCooldownTick--;
    	    
	        if (moveItemCooldownTick <= 0) {
    	        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, EnvironmentalAccumulator.BEAM_INACTIVE, 2);
    	    }
	    }
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
	    super.readFromNBT(compound);
	    
	    degradation = compound.getInteger("degradation");
	    
	    cooldownTick = compound.getInteger("cooldownTick");
	    if (cooldownTick > 0)
	        cooldown = true;
	    
	    moveItemTick = compound.getInteger("moveItemTick");
	    if (moveItemTick > 0)
	        movingItem = true;
	    
	    degradationExecutor.readFromNBT(compound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
	    super.writeToNBT(compound);
	    
	    compound.setInteger("degradation", degradation);
	    compound.setInteger("cooldownTick", cooldownTick);
	    compound.setInteger("moveItemTick", moveItemTick);
	    
	    degradationExecutor.writeToNBT(compound);
	}
    
    @Override
    public float getMaxHealth() {
        return getMaxCooldownTick();
    }

    @Override
    public float getHealth() {
        return Math.min(getMaxCooldownTick() - cooldownTick, getMaxCooldownTick());
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
