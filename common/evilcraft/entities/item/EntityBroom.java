package evilcraft.entities.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.network.RemoteKeyHandler;
import evilcraft.api.config.configurable.Configurable;

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
    public double getYOffset() {
    	return -10d;
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
    	if (!worldObj.isRemote && riddenByEntity == null) {
    		player.mountEntity(this);
    	}
    	
    	return true;
    }
    
    @Override
    public void onUpdate() {
    	super.onUpdate();
    	
    	if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)riddenByEntity;
    		
    		// Rotate broom
    		rotationPitch = player.rotationPitch;
    		rotationYaw = player.rotationYaw;
    		setRotation(rotationYaw, rotationPitch);
    		
    		// Handle player movement
    		double pitch = ((player.rotationPitch + 90) * Math.PI) / 180;
    		double yaw = ((player.rotationYaw + 90) * Math.PI) / 180;
    		
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
