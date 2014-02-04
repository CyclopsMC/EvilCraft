package evilcraft.entities.tileentities;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.util.vector.Vector4f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.Helpers;
import evilcraft.blocks.EnvironmentalAccumulator;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;
import evilcraft.items.WeatherContainer;
import evilcraft.items.WeatherContainer.WeatherContainerTypes;

public class TileEnvironmentalAccumulator extends EvilCraftBeaconTileEntity {
    
    private static final double WEATHER_CONTAINER_MIN_DROP_HEIGHT = 1.0;
    private static final double WEATHER_CONTAINER_MAX_DROP_HEIGHT = 5.0;
    private static final double WEATHER_CONTAINER_SPAWN_HEIGHT = 2.0;
    
    @SideOnly(Side.CLIENT)
    private static final Vector4f ACTIVE_INNER_COLOR = new Vector4f(0.48046875F, 0.29296875F, 0.1171875F, 0.13f);
    @SideOnly(Side.CLIENT)
    private static final Vector4f ACTIVE_OUTER_COLOR = new Vector4f(0.30078125F, 0.1875F, 0.08203125F, 0.13f);
    
    @SideOnly(Side.CLIENT)
    private static final Vector4f COOLDOWN_INNER_COLOR = new Vector4f(0, 0, 0, 0.13f);
    @SideOnly(Side.CLIENT)
    private static final Vector4f COOLDOWN_OUTER_COLOR = new Vector4f(0, 0, 0, 0.13f);
    
    private int cooldownTick = 0;
    private boolean cooldown = false;
    
    private int lastMetadata = -1;
    
	public TileEnvironmentalAccumulator() {
	    super();
	    
	    if (Helpers.isClientSide()) {
	        setBeamInnerColor(ACTIVE_INNER_COLOR);
	        setBeamOuterColor(ACTIVE_OUTER_COLOR);
	    }
	}
	
	public int getMaxCooldownTick() {
	    return EnvironmentalAccumulatorConfig.tickCooldown;
	}
	
	@Override
	public void updateEntity() {
	    if (!worldObj.isRemote) {
	        
	        cooldownTick--;
	        
	        if (cooldown && cooldownTick <= 0)
	            deactivateCooldown();
	        
	        if (!cooldown)
	            updateEnvironmentalAccumulator();
	    } else {
	        setBeamColors(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
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
                    dropWeatherContainer();
                    activateCooldown();
                }
                
                i++;
            }
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
	    if (lastMetadata != metadata) {
	        if (worldObj.isRemote) { 
        	    setBeamInnerColor((metadata != 1) ? ACTIVE_INNER_COLOR : COOLDOWN_INNER_COLOR);
        	    setBeamOuterColor((metadata != 1) ? ACTIVE_OUTER_COLOR : COOLDOWN_OUTER_COLOR);
	        }
    	    
    	    lastMetadata = metadata;
	    }
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
	    super.readFromNBT(compound);
	    
	    cooldownTick = compound.getInteger("cooldownTick");
	    if (cooldownTick > 0)
	        cooldown = true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
	    super.writeToNBT(compound);
	    
	    compound.setInteger("cooldownTick", cooldownTick);
	}
}
