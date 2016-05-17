package org.cyclops.evilcraft.entity.item;

import com.google.common.base.Optional;
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
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.api.broom.BroomModifier;
import org.cyclops.evilcraft.api.broom.BroomModifiers;
import org.cyclops.evilcraft.api.broom.IBroom;
import org.cyclops.evilcraft.client.particle.EntityColoredSmokeFX;
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

    private static final DataParameter<Optional<ItemStack>> ITEMSTACK_INDEX = EntityDataManager.<Optional<ItemStack>>createKey(EntityWeatherContainer.class, DataSerializers.OPTIONAL_ITEM_STACK);

    /**
     * Speed for the broom (in all directions)
     */
    public static final double SPEED = 0.4;
    
    /**
     * Maximum angle of the broom between the XZ-plane and the Y-axis 
     * (in degrees, -90 = completely up, +90 = completely down)
     * This limits the angle under which the player can move up or down
     */
    public static final float MAX_ANGLE = 60.0F;
    /**
     * Minimum angle of the broom between the XZ-plane and the Y-axis 
     * (in degrees, -90 = completely up, +90 = completely down)
     * This limits the angle under which the player can move up or down
     */
    public static final float MIN_ANGLE = -60.0F;
    
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
		return !isDead;
	}
    
    @Override
    public boolean processInitialInteract(EntityPlayer player, ItemStack stack, EnumHand hand) {
        if (!this.worldObj.isRemote && !isBeingRidden() && !player.isSneaking()) {
            player.startRiding(this);
            lastMounted = player;
        }
    	return true;
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (!this.worldObj.isRemote) {
            if (!entityIn.noClip && !this.noClip) {
                Entity controlling = this.getControllingPassenger();
                if (entityIn != controlling) {
                    if (entityIn instanceof EntityLivingBase
                            && !(entityIn instanceof EntityPlayer)
                            && controlling == null
                            && !entityIn.isRiding()) {
                        entityIn.startRiding(this);
                    }
                }
            }
        }
    }

    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean isTeleport) {
        posRotationIncrements += 6;
        
        //this.yOffset = 0.0F;
        this.newPosX = x;
        this.newPosY = y;
        this.newPosZ = z;
        this.newRotationYaw = (double)yaw;
        this.newRotationPitch = (double)pitch;
        this.newPosRotationIncrements = posRotationIncrements;
    }

    @Override
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!this.worldObj.isRemote && !this.isDead) {
            if (this.isEntityInvulnerable(source)) {
                return false;
            }
            if (source.getEntity() instanceof EntityPlayer) {
                if (isBeingRidden()) {
                    this.dismountRidingEntity();
                }
                setDead();
                EntityPlayer player = (EntityPlayer) source.getEntity();
                if (!player.capabilities.isCreativeMode) {
                    ItemStack itemStack = getBroomStack().copy();
                    this.entityDropItem(itemStack, 0.0F);
                }
            }
        }
        return true;
    }

    @Override
    public void onUpdate() {
    	super.onUpdate();

        Entity rider = getControllingPassenger();
        if (!worldObj.isRemote && !isBeingRidden() && lastMounted != null) {
    		// The player dismounted, give him his broom back if he's not in creative mode
    		if (lastMounted instanceof EntityPlayer && Configs.isEnabled(BroomConfig.class)) {
                EntityPlayer player = (EntityPlayer) lastMounted;
                // Return to inventory if we have space and the player is not dead, otherwise drop it on the ground
                if (!player.isDead && (!MinecraftHelpers.isPlayerInventoryFull(player) || player.capabilities.isCreativeMode)) {
                    // Return to inventory if he's not in creative mode
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.addItemStackToInventory(getBroomStack());
                    }
                    worldObj.removeEntity(this);
                }
    		}
    		
            lastMounted = null;
    		
    	} else if (rider instanceof EntityLivingBase) {
            /*
             * TODO: if we ever have the problem that a player can dismount without
             * getting the broom back in his inventory and removing the entity from the world
             * its probably because of this next line of code because onUpdate() is called AFTER
             * the player is dismounted, thus lastMounted is not updated before the player dismounts
             * and thus the dismounting code is never executed
             */
            lastMounted = (EntityLivingBase) rider;
            
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

            if(MinecraftHelpers.isClientSide() && getModifier(BroomModifiers.PARTICLES) > 0) {
                showParticles(this);
            }

            // Apply collisions
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.2, 0.0, 0.2));
            if (list != null && !list.isEmpty()) {
                for (int l = 0; l < list.size(); ++l) {
                    Entity entity = list.get(l);
                    if (entity != rider && entity.canBePushed() && !(entity instanceof EntityBroom)) {
                        for (Map.Entry<BroomModifier, Float> entry : getModifiers().entrySet()) {
                            for (BroomModifier.ICollisionListener listener : entry.getKey().getCollisionListeners()) {
                                listener.onCollide(this, entity, entry.getValue());
                            }
                        }
                        entity.applyEntityCollision(this);

                        // Slow the broom down a bit
                        float toughnessModifier = Math.min(1F, 0.5F + (getModifier(BroomModifiers.STURDYNESS) / (BroomModifiers.STURDYNESS.getMaxTierValue() * 1.5F) / 2F));
                        setLastPlayerSpeed(getLastPlayerSpeed() * toughnessModifier);
                    }
                }
            }

            for (Map.Entry<BroomModifier, Float> entry : getModifiers().entrySet()) {
                for (BroomModifier.ITickListener listener : entry.getKey().getTickListeners()) {
                    listener.onTick(this, entry.getValue());
                }
            }
    	} else {
            if(!this.worldObj.isRemote && rider == null) {
                this.collideWithNearbyEntities();
            }
    	    updateUnmounted();
    	}
    }

    @SideOnly(Side.CLIENT)
    public void showParticles(EntityBroom broom) {
        World world = broom.worldObj;
        if (world.isRemote && broom.lastMounted.moveForward != 0) {
            // Emit particles
            int particles = (int) (broom.getModifier(BroomModifiers.PARTICLES) * (float) broom.getLastPlayerSpeed());
            Triple<Float, Float, Float> color = BroomModifier.getAverageColor(broom.getModifiers());
            for (int i = 0; i < particles; i++) {
                float r = color.getLeft();
                float g = color.getMiddle();
                float b = color.getRight();
                EntityColoredSmokeFX smoke = new EntityColoredSmokeFX(world,
                        broom.posX - broom.motionX * 1.5D + Math.random() * 0.4D - 0.2D,
                        broom.posY - broom.motionY * 1.5D + Math.random() * 0.4D - 0.2D,
                        broom.posZ - broom.motionZ * 1.5D + Math.random() * 0.4D - 0.2D,
                        r, g, b,
                        broom.motionX / 10, broom.motionY / 10, broom.motionZ / 10);
                Minecraft.getMinecraft().effectRenderer.addEffect(smoke);
            }
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

    public boolean canConsume(int amount, EntityLivingBase entityLiving) {
        ItemStack broomStack = getBroomStack();
        return broomStack != null && broomStack.getItem() instanceof IBroom
                && ((IBroom) broomStack.getItem()).canConsumeBroomEnergy(amount, broomStack, entityLiving);
    }

    public void consume(int amount, EntityLivingBase entityLiving) {
        float efficiencyFactor = Math.min(0.9F, Math.max(0.0F, getModifier(BroomModifiers.EFFICIENCY) / BroomModifiers.EFFICIENCY.getMaxTierValue()));
        if(worldObj.rand.nextFloat() > efficiencyFactor) {
            ItemStack broomStack = getBroomStack();
            ((IBroom) broomStack.getItem()).canConsumeBroomEnergy(amount, broomStack, entityLiving);
            setBroomStack(broomStack);
        }
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
        float maneuverabilityFactor = 1F - (getModifier(BroomModifiers.MANEUVERABILITY) / 2000F);
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
        double playerSpeed = lastMounted.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
        playerSpeed += getModifier(BroomModifiers.SPEED) / 100;
        int amount = BroomConfig.bloodUsage;
        EntityLivingBase currentRidingEntity = getControllingPassenger() instanceof EntityLivingBase ? (EntityLivingBase) getControllingPassenger() : null;
        float moveForward = canConsume(amount, currentRidingEntity) ? lastMounted.moveForward : lastMounted.moveForward / 10F;
        playerSpeed *= moveForward;
        if(moveForward != 0) {
            consume(amount, currentRidingEntity);
        }

        // Apply acceleration modifier
        float slowingFactor = 1F - ((getModifier(BroomModifiers.ACCELERATION) + 1F) / 2500F);
        playerSpeed = playerSpeed * (1D - slowingFactor) + lastPlayerSpeed * slowingFactor;

        // Apply levitation modifier
        float levitationModifier = 1;
        if(y > 0) {
            float levitation = getModifier(BroomModifiers.LEVITATION);
            levitationModifier = Math.min(1F, ((levitation * 50F) / Math.max(1F, (float) posY - 64F))
                    / BroomModifiers.LEVITATION.getMaxTierValue());
        }

        motionX = motionX / 10 + x * SPEED * playerSpeed;
        motionY = motionY / 10 + y * SPEED * playerSpeed * levitationModifier;
        motionZ = motionZ / 10 + z * SPEED * playerSpeed;
        if (this.inWater) {
            // (1 -> 0) Lower is better
            float waterMovementFactor = 1F - MathHelper.clamp_float(
                    getModifier(BroomModifiers.SWIMMING) / (BroomModifiers.SWIMMING.getMaxTierValue() * 1.1F), 0F, 1F);
            motionX /= 1 + 4 * waterMovementFactor;
            motionY /= 1 + 4 * waterMovementFactor;
            motionZ /= 1 + 4 * waterMovementFactor;
            motionY += 0.05F * waterMovementFactor;
        }
        lastPlayerSpeed = playerSpeed;
        
        // Update motion on client side to provide a hovering effect
        if (worldObj.isRemote)
            motionY += getHoverOffset();

        moveEntity(motionX, motionY, motionZ);
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
        dataManager.register(ITEMSTACK_INDEX, Optional.of(new ItemStack(Broom.getInstance())));
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
        dataManager.set(ITEMSTACK_INDEX, Optional.fromNullable(itemStack));
    }

    public ItemStack getBroomStack() {
        ItemStack itemStack = dataManager.get(ITEMSTACK_INDEX).or(new ItemStack(Broom.getInstance()));
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
