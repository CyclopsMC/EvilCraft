package evilcraft.entities.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.Configurable;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.network.RemoteKeyHandler;

public class EntityBroom extends Entity implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.ENTITY_NO_GLOBAL_ID;
    
    public static final double SPEED_Y = 0.4;
    
    // The entity who last mounted the broom
    private EntityPlayer lastMounted;

    // Set a configuration for this entity
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    public EntityBroom(World world) {
        super(world);
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
    	if (!worldObj.isRemote && riddenByEntity == null) {
    		player.mountEntity(this);
    		lastMounted = player;
    	}
    	
    	return true;
    }
    
    @Override
    public void onUpdate() {
    	super.onUpdate();
    	
    	if (riddenByEntity == null && lastMounted != null) {
    		// Clear the hotkey entries for this user
    		RemoteKeyHandler.getInstance().clearKeyData(
    				lastMounted.username, 
    				new String[] { "key.evilcraft.broom.up", "key.evilcraft.broom.down" }
    		);
    		
    		lastMounted = null;
    		
    	} else if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)riddenByEntity;
    		
    		// TODO: handle forward/backward movement
			
    		// Handle up or down movement
    		RemoteKeyHandler handler = RemoteKeyHandler.getInstance();
			
			boolean pressedUp = handler.isKeyPressed(player.username, "key.evilcraft.broom.up");
			boolean pressedDown = handler.isKeyPressed(player.username,  "key.evilcraft.broom.down");
			
			if (pressedUp && pressedDown || !(pressedUp || pressedDown)) {
				motionY = 0;
			} else if (pressedUp && worldObj.isAirBlock((int)posX, (int)posY + 1, (int)posZ)) {
				motionY = SPEED_Y;
			} else if (pressedDown && worldObj.isAirBlock((int)posX, (int)posY - 1, (int)posZ)) { 
				motionY = -SPEED_Y;
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
