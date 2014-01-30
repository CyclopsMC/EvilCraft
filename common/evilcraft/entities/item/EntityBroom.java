package evilcraft.entities.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.Helpers;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.items.Broom;

/**
 * Entity for a broom
 * 
 * @author immortaleeb
 *
 */
public class EntityBroom extends Entity implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.ENTITY_NO_GLOBAL_ID;
    
    // Speed for the broom (in all directions)
    public static final double SPEED = 0.4;
    
    // Maximum and minimum angle of the broom between the XZ-plane and the Y-axis 
    // (in degrees, -90 = completely up, +90 = completely down)
    // This limits the angle under which the player can move up or down
    public static final float MAX_ANGLE = 45.0F;	
    public static final float MIN_ANGLE = -45.0F;
    
    // The player that last mounted this broom (used to detect dismounting)
    public EntityPlayer lastMounted = null;
    
    // Set a configuration for this entity
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    public EntityBroom(World world) {
        super(world);
    }
    
    @Override
    public double getMountedYOffset() {
    	return 0.0;
    }
    
    @SideOnly(Side.CLIENT)
    public EntityBroom(World world, double x, double y, double z) {
        super(world);
        setPosition(x, y, z);
    }

    @Override
    public String getUniqueName() {
        return "entities.item."+eConfig.NAMEDID;
    }
    
    @Override
    public boolean isEntity() {
        return true;
    }
    
    @Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}
    
    @Override
    public boolean interactFirst(EntityPlayer player) {
    	mountEntity(player);
    	return true;
    }
    
    @Override
    public void mountEntity(Entity entity) {
        if (!worldObj.isRemote && riddenByEntity == null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            
            player.mountEntity(this);
            lastMounted = player;
        }
    }
    
    @Override
    public void onUpdate() {
    	super.onUpdate();
    	
    	if (!worldObj.isRemote && riddenByEntity == null && lastMounted != null) {
    		// The player dismounted, give him his broom back if he's not in creative mode
    		if (!lastMounted.capabilities.isCreativeMode) {
    		    // Return to inventory if we have space, otherwise drop it on the ground
    		    if (!Helpers.isPlayerInventoryFull(lastMounted))
    		        lastMounted.inventory.addItemStackToInventory(new ItemStack(Broom.getInstance(), 1));
    		    else
    		        dropItem(Broom.getInstance().itemID, 1);
    		}
    		
            lastMounted = null;
    		
    		worldObj.removeEntity(this);
    		
    	} else if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)riddenByEntity;
    		
    		/*
    		 * TODO: if we ever have the problem that a player can dismount without
    		 * getting the broom back in his inventory and removing the entity from the world
    		 * its probably because of this next line of code because onUpdate() is called AFTER
    		 * the player is dismounted, thus lastMounted is not updated before the player dismounts
    		 * and thus the dismounting code is never executed
    		 */
    		lastMounted = player;
    		
    		// Rotate broom
    		rotationPitch = player.rotationPitch;
    		rotationYaw = player.rotationYaw;
    		
    		// Limit the angle under which the player can move up or down
    		if (rotationPitch > MAX_ANGLE)
    			rotationPitch = MAX_ANGLE;
    		else if (rotationPitch < MIN_ANGLE)
    			rotationPitch = MIN_ANGLE;
    		
    		setRotation(rotationYaw, rotationPitch);
    		
    		// Handle player movement
    		double pitch = ((rotationPitch + 90) * Math.PI) / 180;
    		double yaw = ((rotationYaw + 90) * Math.PI) / 180;
    		
    		double x = Math.sin(pitch) * Math.cos(yaw);
    		double z = Math.sin(pitch) * Math.sin(yaw);
    		double y = Math.cos(pitch);
    		
    		if (player.moveForward != 0) {
    			motionX = x * SPEED * player.moveForward;
    			motionY = y * SPEED * player.moveForward;
    			motionZ = z * SPEED * player.moveForward;
    		} else {
    			motionX = 0;
    			motionY = 0;
    			motionZ = 0;
    		}
        	
        	setPosition(posX + motionX, posY + motionY, posZ + motionZ);
    	}
    	
    }

    @Override
    protected void entityInit() {
    	
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    	
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    	
    }
    
    
}
