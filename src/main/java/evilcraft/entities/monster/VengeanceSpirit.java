package evilcraft.entities.monster;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.render.particle.EntityBlurFX;
import evilcraft.render.particle.EntityDarkSmokeFX;

/**
 * A silverfish for the nether.
 * @author rubensworks
 *
 */
public class VengeanceSpirit extends EntityMob implements Configurable {
	
    protected ExtendedConfig<?> eConfig = null;

    /**
     * The type for this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.MOB;
    
    /**
     * The maximum life duration in ticks the spirits should have.
     */
    public static final int REMAININGLIFE_MAX = 1000;
    
    private static final int WATCHERID_INNER = 20;
    private static final int WATCHERID_REMAININGLIFE = 21;
    private static final int WATCHERID_FROZENDURATION = 22;
    private static final int WATCHERID_GLOBALVENGEANCE = 23;
    private static final int WATCHERID_VENGEANCEPLAYERS = 24;

	private EntityLivingBase innerEntity = null;

	@SuppressWarnings("rawtypes")
    @Override
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    /**
     * Make a new instance.
     * @param world The world.
     */
    public VengeanceSpirit(World world) {
        super(world);
        this.getNavigator().setAvoidsWater(false);
        this.stepHeight = 5.0F;
        this.isImmuneToFire = true;
        this.preventEntitySpawning = false;
        
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
        
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWander(this, 1.0F));
        this.tasks.addTask(2, new EntityAILookIdle(this));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.5D, true));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false));
        
        setRemainingLife(world.rand.nextInt(REMAININGLIFE_MAX));
        setFrozenDuration(0);
    }
    
    @Override
	public void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(WATCHERID_INNER, new String());
        this.dataWatcher.addObject(WATCHERID_REMAININGLIFE, 0);
        this.dataWatcher.addObject(WATCHERID_FROZENDURATION, 0);
        this.dataWatcher.addObject(WATCHERID_GLOBALVENGEANCE, 0);
        this.dataWatcher.addObject(WATCHERID_VENGEANCEPLAYERS, new String());
    }
    
    @Override
	public void writeEntityToNBT(NBTTagCompound tag) {
    	super.writeEntityToNBT(tag);
    	if(getInnerEntity() != null)
    		tag.setString("innerEntity", getInnerEntity().getClass().getName());
    	tag.setInteger("remainingLife", getRemainingLife());
    	tag.setInteger("frozenDuration", getFrozenDuration());
    }
    
    @Override
	public void readEntityFromNBT(NBTTagCompound tag) {
    	super.readEntityFromNBT(tag);
    	String name = tag.getString("innerEntity");
    	if(name != null)
    		this.dataWatcher.updateObject(WATCHERID_INNER, name);
    	setRemainingLife(tag.getInteger("remainingLife"));
    	setFrozenDuration(tag.getInteger("frozenDuration"));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.0D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
    }
    
    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 2.0F;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public String getUniqueName() {
        return "entities.monster."+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return true;
    }
    
    @Override
    public boolean attackEntityAsMob(Entity entity) {
        this.setDead();
        this.worldObj.removeEntity(this);
        return super.attackEntityAsMob(entity);
    }
    
    @Override
    public void setDead() {
    	super.setDead();
    	if(worldObj.isRemote) {
    		spawnSmoke();
    	}
    }
    
    @Override
    public boolean isMovementBlocked() {
    	return isFrozen();
    }
    
    @Override
    public void onLivingUpdate() {
    	super.onLivingUpdate();
    	
        if(innerEntity != null && isVisible()) {
        	innerEntity.isDead = isDead;
        	innerEntity.deathTime = deathTime;
        	innerEntity.attackTime = attackTime;
        	innerEntity.hurtTime = hurtTime;
        	innerEntity.rotationPitch = rotationPitch;
        	innerEntity.rotationYaw = rotationYaw;
        	innerEntity.rotationYawHead = rotationYawHead;
        	innerEntity.renderYawOffset = renderYawOffset;
        	innerEntity.cameraPitch = cameraPitch;
        	innerEntity.prevCameraPitch = prevCameraPitch;
        	innerEntity.prevRenderYawOffset = prevRenderYawOffset;
        	innerEntity.prevRotationPitch = prevRotationPitch;
        	innerEntity.prevRotationYaw = prevRotationYaw;
        	innerEntity.prevRotationYawHead = prevRotationYawHead;
        	
        	if(isVisible() && worldObj.isRemote)
        		spawnSmoke();
        }
        
        if(isFrozen()) {
        	this.motionX = 0;
        	this.motionY = 0;
        	this.motionZ = 0;
        	addFrozenDuration(-1);
        	// TODO: render entangled particles
        } else {
	        setRemainingLife(getRemainingLife() - 1);
	        if(getRemainingLife() <= 0) {
	        	this.setDead();
	        	worldObj.removeEntity(this);
	        }
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void spawnSmoke() {
    	int numParticles = rand.nextInt(5);
    	if(this.isDead)
    		numParticles *= 10;
    	float clearRange = width; // Particles can't spawn within this X and Z distance
    	for (int i=0; i < numParticles; i++) {            
            double particleX = posX - width /2 + width * rand.nextFloat();
            if(particleX < 0.7F && particleX >= 0) particleX += width /2;
            if(particleX > -0.7F && particleX <= 0) particleX -= width /2;
            double particleY = posY + height * rand.nextFloat();
            double particleZ = posZ - width / 2 + width * rand.nextFloat();
            if(particleZ < clearRange && particleZ >= 0) particleZ += width /2;
            if(particleZ > -clearRange && particleZ <= 0) particleZ -= width /2;
            
            float particleMotionX = (-0.5F + rand.nextFloat()) * 0.05F;
            float particleMotionY = (-0.5F + rand.nextFloat()) * 0.05F;
            float particleMotionZ = (-0.5F + rand.nextFloat()) * 0.05F;
            
            EntityDarkSmokeFX particle = new EntityDarkSmokeFX(worldObj, particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
            if(this.isDead)
            	particle.setDeathParticles();
            particle.setLiving((float)getRemainingLife() / (float)REMAININGLIFE_MAX);
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
        }
    }
    
    /**
     * If this entity is visible to the current player
     * @return If it is visible
     */
    public boolean isVisible() {
    	return worldObj.isRemote &&
    			(isAlternativelyVisible() || isEnabledVengeance(Minecraft.getMinecraft().thePlayer));
    }
    
    private boolean isAlternativelyVisible() {
    	// TODO: add other possibilities like glasses
		return Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode;
	}

	@Override
    public boolean canBePushed() {
        return false;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    
    @Override
    public void onStruckByLightning(EntityLightningBolt lightning) {
    	setGlobalVengeance(true);
    }
    
    @Override
    public boolean canEntityBeSeen(Entity entity) {
    	if(entity instanceof EntityPlayer)
    		return isEnabledVengeance((EntityPlayer) entity);
    	else
    		return super.canEntityBeSeen(entity);
    }
    
    /**
     * If the given player is vengeanced by this spirit
     * @param player the player.
     * @return If it should be visible.
     */
    public boolean isEnabledVengeance(EntityPlayer player) {
		return isGlobalVengeance() || ArrayUtils.contains(getVengeancePlayers(), player.getDisplayName());
	}
    
    /**
     * Enable vengeance of this spirit for the given player.
     * @param player This player will be added to the target list.
     * @param enabled If vengeance should be enabled
     */
    public void setEnabledVengeance(EntityPlayer player, boolean enabled) {
    	String[] players = getVengeancePlayers();
    	int index = ArrayUtils.indexOf(players, player.getDisplayName());
    	if(enabled && index == ArrayUtils.INDEX_NOT_FOUND)
    		players = ArrayUtils.add(players, player.getDisplayName());
    	else if(!enabled && index != ArrayUtils.INDEX_NOT_FOUND)
    		players = ArrayUtils.remove(players, index);
    	setVengeancePlayers(players);
	}

	/**
     * Get the inner entity.
     * @return inner entity
     */
    @SuppressWarnings("unchecked")
	public EntityLivingBase getInnerEntity() {
    	if(innerEntity != null)
    		return innerEntity;
    	try {
			Class<EntityLivingBase> clazz = (Class<EntityLivingBase>) Class.forName(dataWatcher.getWatchableObjectString(WATCHERID_INNER));
			String name = (String) EntityList.classToStringMapping.get(clazz);
			innerEntity = (EntityLivingBase) EntityList.createEntityByName(name, worldObj);
			this.setSize(innerEntity.width, innerEntity.height);
			return innerEntity;
    	} catch (ClassNotFoundException e) {
    		this.worldObj.removeEntity(this);
		} catch (NullPointerException e) {
			EvilCraft.log("Tried to spirit invalid entity, removing it now.", Level.ERROR);
     		this.worldObj.removeEntity(this);
 		}
    	return null;
    }
    
    /**
     * Set the inner entity;
     * @param innerEntity inner entity
     */
    public void setInnerEntity(EntityLivingBase innerEntity) {
		this.dataWatcher.updateObject(WATCHERID_INNER, innerEntity.getClass().getName());
	}

    /**
     * Get the remaining life.
     * @return The remaining life.
     */
    public int getRemainingLife() {
		return dataWatcher.getWatchableObjectInt(WATCHERID_REMAININGLIFE);
	}

    /**
     * Set the remaining life.
     * @param remainingLife The remaining life.
     */
	public void setRemainingLife(int remainingLife) {
		this.dataWatcher.updateObject(WATCHERID_REMAININGLIFE, remainingLife);
	}

	/**
	 * Get the frozen duration.
	 * @return The frozen duration.
	 */
	public int getFrozenDuration() {
		return dataWatcher.getWatchableObjectInt(WATCHERID_FROZENDURATION);
	}

	/**
	 * Set the frozen duration.
	 * @param frozenDuration The frozen duration.
	 */
	public void setFrozenDuration(int frozenDuration) {
		this.dataWatcher.updateObject(WATCHERID_FROZENDURATION, frozenDuration);
	}
	
	/**
	 * Is this spirit globally vengeancable.
	 * @return Is globally vengeancable.
	 */
	public boolean isGlobalVengeance() {
		return dataWatcher.getWatchableObjectInt(WATCHERID_GLOBALVENGEANCE) == 1;
	}

	/**
	 * Set if this spirit globally vengeancable.
	 * @param globalVengeance Is globally vengeancable.
	 */
	public void setGlobalVengeance(boolean globalVengeance) {
		this.dataWatcher.updateObject(WATCHERID_GLOBALVENGEANCE, globalVengeance?1:0);
	}
	
	/**
	 * Get the vengeanced players.
	 * @return The vengeanced players by display name.
	 */
	public String[] getVengeancePlayers() {
		String encodedPlayers = dataWatcher.getWatchableObjectString(WATCHERID_VENGEANCEPLAYERS);
		return encodedPlayers.split("&");
	}

	/**
	 * Set the vengeanced players.
	 * @param vengeancePlayers The vengeanced players by display name.
	 */
	public void setVengeancePlayers(String[] vengeancePlayers) {
		this.dataWatcher.updateObject(WATCHERID_VENGEANCEPLAYERS, StringUtils.join(vengeancePlayers, "&"));
	}

	/**
     * If the given entity can be 'spiritted'
     * @param entityLiving The entity to check.
     * @return If it can become a spirit.
     */
	public static boolean canSustain(EntityLivingBase entityLiving) {
		// TODO: make better, with blacklist & stuff?
		return !(entityLiving instanceof VengeanceSpirit);
	}

	/**
	 * Add a frozen duration.
	 * @param addFrozen Ticks to add frozen.
	 */
	public void addFrozenDuration(int addFrozen) {
		this.setFrozenDuration(this.getFrozenDuration() + addFrozen);
	}
	
	/**
	 * If this spirit is frozen.
	 * @return If frozen duration larger than zero.
	 */
	public boolean isFrozen() {
		return this.getFrozenDuration() > 0;
	}
	
	/**
	 * When this spirit is hit by a neutron.
	 * @param hitX Hit X.
	 * @param hitY Hit Y.
	 * @param hitZ Hit Z.
	 * @param impactMotionX The motion speed for X.
	 * @param impactMotionY The motion speed for Y.
	 * @param impactMotionZ The motion speed for Z.
	 */
	public void onHit(double hitX, double hitY, double hitZ,
			double impactMotionX, double impactMotionY, double impactMotionZ) {
		addFrozenDuration(worldObj.rand.nextInt(2) + 1);
		showBurstParticles(hitX, hitY, hitZ, impactMotionX, impactMotionY, impactMotionZ);
	}

	private void showBurstParticles(double hitX, double hitY, double hitZ, 
			double impactMotionX, double impactMotionY, double impactMotionZ) {
		for(int i = 0; i < worldObj.rand.nextInt(5); i++) {
			float scale = 0.04F - rand.nextFloat() * 0.02F;
	    	float red = rand.nextFloat() * 0.2F + 0.3F;
	        float green = rand.nextFloat() * 0.2F + 0.3F;
	        float blue = rand.nextFloat() * 0.01F;
	        float ageMultiplier = (float) (rand.nextDouble() * 0.5D + 3D);
	        
	        double dx = 0.1D - rand.nextDouble() * 0.2D - impactMotionX * 0.1D;
	        double dy = 0.1D - rand.nextDouble() * 0.2D - impactMotionY * 0.1D;
	        double dz = 0.1D - rand.nextDouble() * 0.2D - impactMotionZ * 0.1D;
	        
			EntityBlurFX blur = new EntityBlurFX(worldObj, hitX, hitY, hitZ, scale,
					dx, dy, dz, red, green, blue, ageMultiplier);
			Minecraft.getMinecraft().effectRenderer.addEffect(blur);
		}
	}
    
}
