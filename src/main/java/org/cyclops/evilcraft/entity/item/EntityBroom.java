package org.cyclops.evilcraft.entity.item;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.core.broom.BroomParts;
import org.cyclops.evilcraft.core.helper.MathHelpers;
import org.cyclops.evilcraft.item.Broom;
import org.cyclops.evilcraft.item.BroomConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Entity for a broom
 * 
 * @author immortaleeb
 *
 */
public class EntityBroom extends Entity implements IConfigurable{

    private static final int ITEMSTACK_INDEX = 15;

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
    public EntityLivingBase lastMounted = null;

    private boolean setLast = false;
    private double lastPlayerSpeed = 0D;
    private float lastRotationPitch = -1F;
    private float lastRotationYaw = -1F;

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

    private Map<BroomModifier, Float> cachedModifiers = null;

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
    protected boolean canTriggerWalking() {
        return false;
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
    public void applyEntityCollision(Entity entityIn) {
        if (!this.worldObj.isRemote) {
            if (!entityIn.noClip && !this.noClip) {
                if (entityIn != this.riddenByEntity) {
                    if (entityIn instanceof EntityLivingBase
                            && !(entityIn instanceof EntityPlayer)
                            && this.riddenByEntity == null
                            && entityIn.ridingEntity == null) {
                        entityIn.mountEntity(this);
                    }
                }
            }
        }
    }

    @Override
    public void mountEntity(Entity entity) {
        if (riddenByEntity == null && entity instanceof EntityPlayer) {
            if(!worldObj.isRemote) {
                EntityPlayer player = (EntityPlayer) entity;

                player.mountEntity(this);
                lastMounted = player;
            }
            rotationPitch = entity.rotationPitch;
            rotationYaw = entity.rotationYaw;
            lastRotationPitch = rotationPitch;
            lastRotationYaw = rotationYaw;
        }
    }

    @Override
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean isTeleport) {
        posRotationIncrements += 6;
        
        //this.yOffset = 0.0F;
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
            if(lastMounted instanceof EntityPlayer && Configs.isEnabled(BroomConfig.class)) {
                EntityPlayer player = (EntityPlayer) lastMounted;
                // The player dismounted, give him his broom back if he's not dead and if we have space
                if (!player.isDead && (!MinecraftHelpers.isPlayerInventoryFull(player) || player.capabilities.isCreativeMode)) {
                    // Return to inventory if he's not in creative mode
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.addItemStackToInventory(getBroomStack());
                    }
                    worldObj.removeEntity(this);
                }
            }
            lastMounted = null;
    		
    	} else if (riddenByEntity != null && riddenByEntity instanceof EntityLivingBase) {
            /*
             * TODO: if we ever have the problem that a player can dismount without
             * getting the broom back in his inventory and removing the entity from the world
             * its probably because of this next line of code because onUpdate() is called AFTER
             * the player is dismounted, thus lastMounted is not updated before the player dismounts
             * and thus the dismounting code is never executed
             */
            lastMounted = (EntityLivingBase)riddenByEntity;
            
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
            if(!this.worldObj.isRemote && riddenByEntity == null) {
                this.collideWithNearbyEntities();
            }
    	    updateUnmounted();
    	}
    }

    protected void collideWithNearbyEntities() {
        List<Entity> list = this.worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D), Predicates.<Entity>and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
            public boolean apply(Entity p_apply_1_) {
                return p_apply_1_.canBePushed();
            }
        }));

        if (!list.isEmpty()){
            for (Entity entity : list) {
                this.applyEntityCollision(entity);
            }
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

    protected boolean canConsume(int amount, EntityPlayer player) {
        return Broom.getInstance().canConsume(amount, getBroomStack(), player);
    }

    protected void consume(int amount, EntityPlayer player) {
        ItemStack broomStack = getBroomStack();
        Broom.getInstance().consume(amount, broomStack, player);
        setBroomStack(broomStack);
    }
    
    /**
     * Called on the server side for all players or on the client side when the 
     * player mounted on the broom is the local player, so movement is as smooth as
     * possible.
     */
    protected void updateMountedServer() {
        if (!setLast) {
            setLast = true;
            lastRotationYaw = rotationYaw;
            lastRotationPitch = rotationPitch;
        }

        if (!(lastMounted instanceof EntityPlayer)) {
            lastMounted.rotationYaw = lastMounted.rotationYawHead;
            // We have to hardcode this speed because the entity may already have ticked,
            // so we can't count on it having a predictable speed
            lastMounted.moveForward = 0.5F;
        }

        // Rotate broom
        rotationPitch = MathHelpers.normalizeAngle_180(lastMounted.rotationPitch);
        rotationYaw = MathHelpers.normalizeAngle_180(lastMounted.rotationYaw);

        // Apply maneuverability modifier
        float maneuverabilityFactor = 1F / Math.max(1F, getModifier(BroomModifiers.MANEUVERABILITY));
        maneuverabilityFactor = (float) Math.pow(maneuverabilityFactor, 0.01F);
        rotationPitch = rotationPitch * (1F - maneuverabilityFactor) + lastRotationPitch * maneuverabilityFactor;
        // These if's are necessary to fix rotation when the yaw goes over the border of -180F;+180F
        if(lastRotationYaw - rotationYaw > 180F) {
            lastRotationYaw -= 360F;
        }
        if(lastRotationYaw - rotationYaw < -180F) {
            lastRotationYaw += 360F;
        }
        rotationYaw = rotationYaw * (1F - maneuverabilityFactor) + lastRotationYaw * maneuverabilityFactor;
        lastRotationPitch = rotationPitch;
        lastRotationYaw = rotationYaw;
        
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

        // Apply speed modifier
        double playerSpeed = lastMounted.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
        playerSpeed += getModifier(BroomModifiers.SPEED) / 100;
        int amount = BroomConfig.bloodUsage;
        EntityPlayer player = lastMounted instanceof EntityPlayer ? (EntityPlayer) lastMounted : null;
        float moveForward = canConsume(amount, player) ? lastMounted.moveForward : lastMounted.moveForward / 10F;
        playerSpeed *= moveForward;
        if(moveForward != 0) {
            consume(amount, player);
        }

        // Apply acceleration modifier
        double slowingFactor = 1D / Math.max(1D, getModifier(BroomModifiers.ACCELERATION));
        slowingFactor = Math.pow(slowingFactor, 0.01D);
        playerSpeed = playerSpeed * (1D - slowingFactor) + lastPlayerSpeed * slowingFactor;

        // Apply levitation modifier
        float levitationModifier = 1;
        if(y > 0) {
            float levitation = getModifier(BroomModifiers.LEVITATION);
            levitationModifier = Math.min(1F, ((levitation * 50F) / Math.max(1F, (float) posY - 64F))
                    / BroomModifiers.LEVITATION.getMaxTierValue());
        }

        motionX = x * SPEED * playerSpeed;
        motionY = y * SPEED * playerSpeed * levitationModifier;
        motionZ = z * SPEED * playerSpeed;
        lastPlayerSpeed = playerSpeed;
        
        // Update motion on client side to provide a hovering effect
        if (worldObj.isRemote)
            motionY += getHoverOffset();

        moveEntity(motionX, motionY, motionZ);
        
        // Apply collisions
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.2, 0.0, 0.2));
        if (list != null && !list.isEmpty()) {
            for (int l = 0; l < list.size(); ++l) {
                Entity entity = list.get(l);
                if (entity != this.riddenByEntity && entity.canBePushed() && !(entity instanceof EntityBroom)) {
                    for (Map.Entry<BroomModifier, Float> entry : getModifiers().entrySet()) {
                        for (BroomModifier.ICollisionListener listener : entry.getKey().getCollisionListeners()) {
                            listener.onCollide(this, entity, entry.getValue());
                        }
                    }
                    entity.applyEntityCollision(this);
                }
            }
        }

        if (worldObj.isRemote && lastMounted.moveForward != 0) {
            // Emit particles
            int particles = (int) (getModifier(BroomModifiers.PARTICLES) * (float) playerSpeed);
            for(int i = 0; i < particles; i++) {
                EnumParticleTypes particle = EnumParticleTypes.CLOUD;
                if(getModifier(BroomModifiers.FLAME) > 0) {
                    particle = EnumParticleTypes.FLAME;
                }
                worldObj.spawnParticle(particle,
                        posX - x * 1.5D + Math.random() * 0.4D - 0.2D, posY - y * 1.5D + Math.random() * 0.4D - 0.2D, posZ - z * 1.5D + Math.random() * 0.4D - 0.2D,
                        motionX / 10, motionY / 10, motionZ / 10);
            }
        }

        for (Map.Entry<BroomModifier, Float> entry : getModifiers().entrySet()) {
            for (BroomModifier.ITickListener listener : entry.getKey().getTickListeners()) {
                listener.onTick(this, entry.getValue());
            }
        }
    }

    public double getLastPlayerSpeed() {
        return lastPlayerSpeed;
    }

    public void setLastPlayerSpeed(double lastPlayerSpeed) {
        this.lastPlayerSpeed = lastPlayerSpeed;
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
    public void fall(float distance, float damageMultiplier) { } // Makes sure the player doesn't get any fall damage when on the broom

    @Override
    protected void entityInit() {
        dataWatcher.addObject(ITEMSTACK_INDEX, new ItemStack(Broom.getInstance()));
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    	
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    	
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }

    public void setBroomStack(ItemStack itemStack) {
        dataWatcher.updateObject(ITEMSTACK_INDEX, itemStack);
    }

    public ItemStack getBroomStack() {
        ItemStack itemStack = dataWatcher.getWatchableObjectItemStack(ITEMSTACK_INDEX);
        itemStack.stackSize = 1;
        return itemStack;
    }

    public Map<BroomModifier, Float> getModifiers() {
        if (cachedModifiers == null) {
            cachedModifiers = Maps.newHashMap();
            ItemStack broomStack = getBroomStack();
            Map<BroomModifier, Float> modifiers = BroomModifiers.REGISTRY.getModifiers(broomStack);
            Map<BroomModifier, Float> baseModifiers = BroomParts.REGISTRY.getBaseModifiersFromBroom(broomStack);
            Set<BroomModifier> modifierTypes = Sets.newHashSet();
            modifierTypes.addAll(modifiers.keySet());
            modifierTypes.addAll(baseModifiers.keySet());

            for (BroomModifier modifier : modifierTypes) {
                float value = modifier.getDefaultValue();
                if (baseModifiers.containsKey(modifier)) {
                    value = baseModifiers.get(modifier);
                }
                if (modifiers.containsKey(modifier)) {
                    value = modifier.apply(value, Lists.newArrayList(modifiers.get(modifier)));
                }
                cachedModifiers.put(modifier, value);
            }
        }
        return cachedModifiers;
    }

    public float getModifier(BroomModifier modifier) {
        Float value = getModifiers().get(modifier);
        if(value == null) {
            return modifier.getDefaultValue();
        }
        return value;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagCompound broomItemTag = new NBTTagCompound();
        getBroomStack().writeToNBT(broomItemTag);
        tagCompound.setTag("broomItem", broomItemTag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        ItemStack broomStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("broomItem"));
        if (broomStack != null) {
            setBroomStack(broomStack);
        }
    }
}
