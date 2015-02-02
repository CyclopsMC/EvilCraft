package evilcraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Configs;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.helper.MathHelpers;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.item.Broom;
import evilcraft.item.BroomConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;

/**
 * Entity for a broom
 * 
 * @author immortaleeb
 *
 */
public class EntityBroom extends Entity implements IConfigurable{
    
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
     * Maximum amplitude of the cosine functions which generate the hovering effect
     */
    private static final float MAX_COS_AMPLITUDE = 0.2f;
    
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
    
    /**
     * This variable holds the last hover offset
     * that was set by this broom during its last
     * update.
     */
    @SideOnly(Side.CLIENT)
    private double oldHoverOffset;

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
        setSize(1.5f, 0.6f);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        initBroomHoverTickOffset();
    }
    
    protected void initBroomHoverTickOffset() {
        broomHoverTickOffset = rand.nextInt((int)Math.PI);
    }
    
    @Override
    public double getMountedYOffset() {
        return 0.0;
    }
    
    @Override
	public boolean canBeCollidedWith() {
		return !isDead && riddenByEntity == null;
	}
    
    @Override
    public boolean interactFirst(EntityPlayer player) {
        if (riddenByEntity == null) {
            mountEntity(player);
            return true;
        }
        
    	return false;
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
        
        /**
         * If the player on the broom is the same as the client player,
         * then make some corrections for its position based on what the server
         * sent us
         */
        if (worldObj.isRemote && Minecraft.getMinecraft().thePlayer == lastMounted) {
            double dx = newPosX - posX;
            double dy = newPosY - posY + oldHoverOffset;
            double dz = newPosZ - posZ;
            
            boolean changePosition = false;
            
            // Correct positions when the difference between the server and client position gets too big
            if (Math.abs(dx) > EntityBroomConfig.desyncThreshold) {
                posX += dx * EntityBroomConfig.desyncCorrectionValue;
                changePosition = true;
            }
            
            if (Math.abs(dy) > EntityBroomConfig.desyncThreshold) {
                posY += dy * EntityBroomConfig.desyncCorrectionValue;
                changePosition = true;
            }
            
            if (Math.abs(dz) > EntityBroomConfig.desyncThreshold) {
                posZ += dz * EntityBroomConfig.desyncCorrectionValue;
                changePosition = true;
            }
            
            if (changePosition)
                setPosition(posX, posY, posZ);
        }
    }
    
    @Override
    public void onUpdate() {
    	super.onUpdate();

        if (!worldObj.isRemote && riddenByEntity == null && lastMounted != null) {
    		// The player dismounted, give him his broom back if he's not in creative mode
    		if (!lastMounted.capabilities.isCreativeMode && Configs.isEnabled(BroomConfig.class)) {
    		    // Return to inventory if we have space and the player is not dead, otherwise drop it on the ground
                if (!lastMounted.isDead && !MinecraftHelpers.isPlayerInventoryFull(lastMounted))
    		        lastMounted.inventory.addItemStackToInventory(new ItemStack(Broom.getInstance(), 1));
    		    else
    		        dropItem(Broom.getInstance(), 1);
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
            
            float yaw = MathHelpers.normalizeAngle_180((float)(newRotationYaw - rotationYaw));
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
        rotationPitch = MathHelpers.normalizeAngle_180(lastMounted.rotationPitch);
        rotationYaw = MathHelpers.normalizeAngle_180(lastMounted.rotationYaw);
        
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
            double playerSpeed = 10 * lastMounted.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
            
            motionX = x * SPEED * playerSpeed * lastMounted.moveForward;
            motionY = y * SPEED * playerSpeed * lastMounted.moveForward;
            motionZ = z * SPEED * playerSpeed * lastMounted.moveForward;
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

                if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityBroom)
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
        float x = worldObj.getWorldTime();
        float t = broomHoverTickOffset;
        double newHoverOffset = Math.cos(x / 10 + t) * Math.cos(x / 12 + t) * Math.cos(x / 15 + t) * MAX_COS_AMPLITUDE;
        
        double newHoverDifference = newHoverOffset - oldHoverOffset;
        oldHoverOffset += newHoverDifference;
        
        return newHoverDifference;
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
