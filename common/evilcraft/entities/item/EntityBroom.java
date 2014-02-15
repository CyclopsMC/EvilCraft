package evilcraft.entities.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
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
    
    protected ExtendedConfig<?> eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.ENTITY;
    
    /**
     * Speed for the broom (in all directions)
     */
    public static final double SPEED = 0.4;
    
    /**
     * Maximum angle of the broom between the XZ-plane and the Y-axis 
     * (in degrees, -90 = completely up, +90 = completely down)
     * This limits the angle under which the player can move up or down
     */
    public static final float MAX_ANGLE = 45.0F;	
    /**
     * Minimum angle of the broom between the XZ-plane and the Y-axis 
     * (in degrees, -90 = completely up, +90 = completely down)
     * This limits the angle under which the player can move up or down
     */
    public static final float MIN_ANGLE = -45.0F;
    
    /**
     * The player that last mounted this broom (used to detect dismounting)
     */
    public EntityPlayer lastMounted = null;
    
    private double newPosX;
    private double newPosY;
    private double newPosZ;
    private double newRotationYaw;
    private double newRotationPitch;
    private int newPosRotationIncrements;
    
    // This value adds a random value to the world tick in the calculations of the hover offset of a broom
    // This makes sure that all brooms don't reach the highest and lowest hovering points at the same time
    private int broomHoverTickOffset;
    
    @SuppressWarnings("rawtypes")
    @Override
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    /**
     * Make a new instance in the given world.
     * @param world The world to make it in.
     */
    public EntityBroom(World world) {
        this(world, 0.0, 0.0, 0.0);
        initBroomHoverTickOffset();
    }
    
    /**
     * Make a new instance at the given location in a world.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    public EntityBroom(World world, double x, double y, double z) {
        super(world);
        setPosition(x, y, z);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        initBroomHoverTickOffset();
    }
    
    protected void initBroomHoverTickOffset() {
        broomHoverTickOffset = rand.nextInt((int)(Math.PI * 10));
    }
    
    @Override
    public double getMountedYOffset() {
        return 0.0;
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
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements)
    {
        posRotationIncrements += 6;
        
        this.yOffset = 0.0F;
        this.newPosX = x;
        this.newPosY = y;
        this.newPosZ = z;
        this.newRotationYaw = (double)yaw;
        this.newRotationPitch = (double)pitch;
        this.newPosRotationIncrements = posRotationIncrements;
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
            /*
             * TODO: if we ever have the problem that a player can dismount without
             * getting the broom back in his inventory and removing the entity from the world
             * its probably because of this next line of code because onUpdate() is called AFTER
             * the player is dismounted, thus lastMounted is not updated before the player dismounts
             * and thus the dismounting code is never executed
             */
            lastMounted = (EntityPlayer)riddenByEntity;
            
            prevPosX = posX;
            prevPosY = posY;
            prevPosZ = posZ;
            prevRotationPitch = rotationPitch;
            prevRotationYaw = rotationYaw;
            
    	    if (!worldObj.isRemote || Minecraft.getMinecraft().thePlayer == lastMounted) {
    	        updateMountedServer();   
    	    } else {
    	        updateMountedClient();
    	    }
    	} else {
    	    updateUnmounted();
    	}
    	
    }
    
    protected void updateMountedClient() {
        if (newPosRotationIncrements > 0) {
            double x = posX + (newPosX - posX) / newPosRotationIncrements;
            double y = posY + (newPosY - posY) / newPosRotationIncrements;
            double z = posZ + (newPosZ - posZ) / newPosRotationIncrements;
            
            float yaw = Helpers.normalizeAngle_180((float)(newRotationYaw - rotationYaw));
            rotationYaw += yaw / newPosRotationIncrements;
            rotationPitch += (newRotationPitch - rotationPitch) / newPosRotationIncrements;
            
            newPosRotationIncrements--;
            
            setPosition(x, y, z);
            setRotation(rotationYaw, rotationPitch);
        }
        
        moveEntity(0, getHoverOffset(), 0);
    }
    
    /**
     * Called on the server side for all players or on the client side when the 
     * player mounted on the broom is the local player, so movement is as smooth as
     * possible.
     */
    protected void updateMountedServer() {
        // Rotate broom
        rotationPitch = Helpers.normalizeAngle_180(lastMounted.rotationPitch);
        rotationYaw = Helpers.normalizeAngle_180(lastMounted.rotationYaw);
        
        // Limit the angle under which the player can move up or down
        if (rotationPitch > MAX_ANGLE)
            rotationPitch = MAX_ANGLE;
        else if (rotationPitch < MIN_ANGLE)
            rotationPitch = MIN_ANGLE;
        
        setRotation(rotationYaw, rotationPitch);
        
        // Handle player movement
        double pitch = ((riddenByEntity.rotationPitch + 90) * Math.PI) / 180;
        double yaw = ((riddenByEntity.rotationYaw + 90) * Math.PI) / 180;
        
        double x = Math.sin(pitch) * Math.cos(yaw);
        double z = Math.sin(pitch) * Math.sin(yaw);
        double y = Math.cos(pitch);
        
        if (lastMounted.moveForward != 0) {
            motionX = x * SPEED * lastMounted.moveForward;
            motionY = y * SPEED * lastMounted.moveForward;
            motionZ = z * SPEED * lastMounted.moveForward;
        } else {
            motionX = 0;
            motionY = 0;
            motionZ = 0;
        }
        
        // Update motion on client side to provide a hovering effect
        if (worldObj.isRemote)
            motionY += getHoverOffset();

        moveEntity(motionX, motionY, motionZ);
        
        // Apply collisions
        @SuppressWarnings("rawtypes")
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2, 0.0, 0.2));
        int l;

        if (list != null && !list.isEmpty())
        {
            for (l = 0; l < list.size(); ++l)
            {
                Entity entity = (Entity)list.get(l);

                if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityBoat)
                {
                    entity.applyEntityCollision(this);
                }
            }
        }
    }
    
    protected void updateUnmounted() {
        if (worldObj.isRemote) {
            moveEntity(0, getHoverOffset(), 0);
        }
    }

    protected double getHoverOffset() {
        float x = worldObj.getWorldTime() + broomHoverTickOffset;
        return Math.cos(x / 10) * Math.cos(x / 12) * Math.cos(x / 15) * 0.01f;
    }
    
    @Override
    protected void fall(float par1) { } // Makes sure the player doesn't get any fall damage when on the broom

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
