package evilcraft.entities.tileentities;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.util.vector.Vector4f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.Helpers;
import evilcraft.blocks.EnvironmentalAccumulator;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;
import evilcraft.items.WeatherContainer;
import evilcraft.items.WeatherContainer.WeatherContainerTypes;

public class TileEnvironmentalAccumulator extends EvilCraftBeaconTileEntity {
    
    private static final int ITEM_MOVE_DURATION = 100;
    private static final float ITEM_MOVE_SPEED = 0.3f / 20;
    
    private static final double WEATHER_CONTAINER_MIN_DROP_HEIGHT = 0.0;
    private static final double WEATHER_CONTAINER_MAX_DROP_HEIGHT = 2.0;
    private static final double WEATHER_CONTAINER_SPAWN_HEIGHT = ITEM_MOVE_DURATION * ITEM_MOVE_SPEED + 1;
    
    private int cooldownTick = 0;
    private boolean cooldown = false;
    
    private int moveItemTick = 0;
    private boolean movingItem = false;
    
    private int lastMetadata;
    
    @SideOnly(Side.CLIENT)
    private float movingItemY;
    
	public TileEnvironmentalAccumulator() {
	    super();
	    
	    if (Helpers.isClientSide()) {
	        setBeamInnerColor(getInnerColorByMetadata(EnvironmentalAccumulator.BEAM_ACTIVE));
	        setBeamOuterColor(getOuterColorByMetadata(EnvironmentalAccumulator.BEAM_ACTIVE));
	        
	        movingItemY = -1.0f;
	        lastMetadata = -1;
	    }
	}
	
	@SideOnly(Side.CLIENT)
	private Vector4f getInnerColorByMetadata(int metadata) {
	    if (metadata == EnvironmentalAccumulator.BEAM_ACTIVE)
	        return new Vector4f(0.48046875F, 0.29296875F, 0.1171875F, 0.13f);
	    else if (metadata == EnvironmentalAccumulator.BEAM_INACTIVE)
	        return new Vector4f(0, 0, 0, 0.13f);
	    else
	        return new Vector4f(0.48046875F, 0.29296875F, 0.1171875F, 0.05f);
	}
	
	@SideOnly(Side.CLIENT)
    private Vector4f getOuterColorByMetadata(int metadata) {
        if (metadata == EnvironmentalAccumulator.BEAM_INACTIVE)
            return new Vector4f(0, 0, 0, 0.13f);
        else
            return new Vector4f(0.30078125F, 0.1875F, 0.08203125F, 0.13f);
    }
	
	public int getMaxCooldownTick() {
	    return EnvironmentalAccumulatorConfig.tickCooldown;
	}
	
	@SideOnly(Side.CLIENT)
	public float getMovingItemY() {
	    return movingItemY;
	}
	
	@Override
	public void updateEntity() {
	    if (!worldObj.isRemote) {
	        moveItemTick--;
	        cooldownTick--;
            
	        if (cooldown && cooldownTick <= 0)
	            deactivateCooldown();
	        
	        if (movingItem && moveItemTick <= 0) {
                deactivateMoveItem();
                dropWeatherContainer();
                activateCooldown();
            }
	        
	        if (!cooldown)
	            updateEnvironmentalAccumulator();
	    } else {
	        updateClient();
	    }
	}
	
	private void updateEnvironmentalAccumulator() {
	    if (!worldObj.isRemote) {
	        
	        // Look for items thrown into the beam
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
	        if (lastMetadata == EnvironmentalAccumulator.MOVING_ITEM) {
	            movingItemY += ITEM_MOVE_SPEED;
	        }
	    } else {
    	    if (metadata == EnvironmentalAccumulator.MOVING_ITEM) {
    	        movingItemY = 0f;
    	    } else if (metadata == EnvironmentalAccumulator.BEAM_INACTIVE) {
	            // Stop showing the moving item animation
	            // TODO: make a custom particle effect for this
	            this.worldObj.playAuxSFX(2002, (int)Math.round(xCoord), (int)Math.round(yCoord + WEATHER_CONTAINER_SPAWN_HEIGHT), (int)Math.round(zCoord), 16428);
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
        movingItem = false;
        moveItemTick = 0;
    }
	
	private void activateCooldown() {
	    cooldownTick = getMaxCooldownTick();
	    cooldown = true;
	    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, EnvironmentalAccumulator.BEAM_INACTIVE, 2);
	}
	
	private void deactivateCooldown() {
	    cooldownTick = 0;
	    cooldown = false;
	    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, EnvironmentalAccumulator.BEAM_ACTIVE, 2);
	}
	
	public void setBeamColors(int metadata) {
        if (worldObj.isRemote) { 
    	    setBeamInnerColor(getInnerColorByMetadata(metadata));
    	    setBeamOuterColor(getOuterColorByMetadata(metadata));
        }
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
	    super.readFromNBT(compound);
	    
	    cooldownTick = compound.getInteger("cooldownTick");
	    if (cooldownTick > 0)
	        cooldown = true;
	    
	    moveItemTick = compound.getInteger("moveItemTick");
	    if (moveItemTick > 0)
	        movingItem = true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
	    super.writeToNBT(compound);
	    
	    compound.setInteger("cooldownTick", cooldownTick);
	    compound.setInteger("moveItemTick", moveItemTick);
	}
}
