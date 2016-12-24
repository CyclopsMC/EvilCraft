package org.cyclops.evilcraft.entity.monster;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.experimental.Delegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.client.particle.ParticleBlur;
import org.cyclops.cyclopscore.config.IChangedCallback;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.*;
import org.cyclops.evilcraft.block.GemStoneTorchConfig;
import org.cyclops.evilcraft.client.particle.ParticleDarkSmoke;
import org.cyclops.evilcraft.client.particle.ParticleDegrade;
import org.cyclops.evilcraft.core.helper.obfuscation.ObfuscationHelpers;
import org.cyclops.evilcraft.core.monster.EntityNoMob;
import org.cyclops.evilcraft.item.BurningGemStone;
import org.cyclops.evilcraft.item.BurningGemStoneConfig;
import org.cyclops.evilcraft.item.SpectralGlasses;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * A silverfish for the nether.
 * @author rubensworks
 *
 */
public class VengeanceSpirit extends EntityNoMob implements IConfigurable {
	
	/**
	 * The default I18N key for when no inner entity exists.
	 */
	public static final ResourceLocation DEFAULT_L10N_KEY = new ResourceLocation("vengeance_spirit");
	
    private static final Set<Class<? extends EntityLivingBase>> BLACKLIST = Sets.newHashSet();
    private static final Set<Class<? extends EntityLivingBase>> IMC_BLACKLIST = Sets.newHashSet();
    
    /**
     * The minimum life duration in ticks the spirits should have.
     */
    public static final int REMAININGLIFE_MIN = 250;
    /**
     * The maximum life duration in ticks the spirits should have.
     */
    public static final int REMAININGLIFE_MAX = 1000;

    public static final DataParameter<String> WATCHERID_INNER = EntityDataManager.<String>createKey(VengeanceSpirit.class, DataSerializers.STRING);
    public static final DataParameter<Integer> WATCHERID_REMAININGLIFE = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> WATCHERID_FROZENDURATION = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> WATCHERID_GLOBALVENGEANCE = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<String> WATCHERID_VENGEANCEPLAYERS = EntityDataManager.<String>createKey(VengeanceSpirit.class, DataSerializers.STRING);
    public static final DataParameter<Integer> WATCHERID_ISSWARM = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> WATCHERID_SWARMTIER = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> WATCHERID_BUILDUP = EntityDataManager.<Integer>createKey(VengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<String> WATCHERID_PLAYERID = EntityDataManager.<String>createKey(VengeanceSpirit.class, DataSerializers.STRING);
    public static final DataParameter<String> WATCHERID_PLAYERNAME = EntityDataManager.<String>createKey(VengeanceSpirit.class, DataSerializers.STRING);

    @Getter
    @Delegate
    private VengeanceSpiritSyncedData data;

	private EntityLivingBase innerEntity = null;

    private String preferredInnerEntity;

    public VengeanceSpirit(World world) {
        this(world, null);
    }

    /**
     * Make a new instance.
     * @param world The world.
     */
    public VengeanceSpirit(World world, String preferredInnerEntity) {
        super(world);
        this.preferredInnerEntity = preferredInnerEntity;

        this.stepHeight = 5.0F;
        this.isImmuneToFire = true;
        this.preventEntitySpawning = false;
        this.setSize(1, 1); // Dummy size, to avoid rare bounding box crashes before inner entity is init.

        double speed = 0.25D;
        float damage = 0.5F;
        int remainingLife = MathHelper.getInt(world.rand, REMAININGLIFE_MIN,
        		REMAININGLIFE_MAX);
        if(isSwarm()) {
        	speed += 0.125D * getSwarmTier();
        	damage += 0.5D * getSwarmTier();
        	remainingLife += (REMAININGLIFE_MAX - REMAININGLIFE_MIN) * getSwarmTier();
        }

        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(speed);

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(2, new EntityAILookIdle(this));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, damage));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));

        setRemainingLife(remainingLife);
        setFrozenDuration(0);
    }
    
    @Override
	public void entityInit() {
        super.entityInit();
        if (preferredInnerEntity == null)
            data = new VengeanceSpiritSyncedData(this.dataManager, VengeanceSpiritData.getRandomInnerEntity(this.rand));
        else
            data = new VengeanceSpiritSyncedData(this.dataManager, preferredInnerEntity);
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
    	tag = super.writeToNBT(tag);
    	data.writeNBT(tag);
        return tag;
    }
    
    @Override
	public void readEntityFromNBT(NBTTagCompound tag) {
    	super.readEntityFromNBT(tag);
        data.readNBT(tag);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3125D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
    }

    @Override
    protected ResourceLocation getLootTable() {
        return new ResourceLocation(Reference.MOD_ID, "entities/" + VengeanceSpiritConfig._instance.getNamedId());
    }
    
    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() / 3.0F;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource damageSource) {
        return !(damageSource instanceof ExtendedDamageSource.VengeanceBeamDamageSource);
    }
    
    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if(getBuildupDuration() > 0) return false; // Don't attack anything when still building up.

        this.setDead();
        this.world.removeEntity(this);
    	if(entity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer) entity;

            if(Loader.isModLoaded(Reference.MOD_THAUMCRAFT)) {
                addWarp((EntityPlayer) entity);
            }

    		if(!Configs.isEnabled(BurningGemStoneConfig.class)
    				|| BurningGemStone.damageForPlayer(player, isSwarm() ? getSwarmTier() : 0, false)) {
    			entity.addVelocity(
    					(double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * 0.01F),
    					0.025D,
    					(double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * 0.01F));
    			entity.attackEntityFrom(DamageSource.causeMobDamage(this), 0.1F);
    			return false;
    		}
    	}
        return super.attackEntityAsMob(entity);
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, @Nullable DamageSource damageSource) {
        super.dropLoot(wasRecentlyHit, lootingModifier, damageSource);

        // Also drop loot from inner entity!
        EntityLivingBase innerEntity = getInnerEntity();
        if (innerEntity instanceof EntityLiving) {
            ResourceLocation deathLootTable = ObfuscationHelpers.getLootTable((EntityLiving) innerEntity);
            if (deathLootTable != null) {
                LootTable loottable = getEntityWorld().getLootTableManager().getLootTableFromLocation(deathLootTable);
                LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer) getEntityWorld()))
                        .withLootedEntity(innerEntity)
                        .withDamageSource(DamageSource.GENERIC);

                if (wasRecentlyHit && this.attackingPlayer != null) {
                    lootcontext$builder = lootcontext$builder.withPlayer(this.attackingPlayer).withLuck(this.attackingPlayer.getLuck());
                }

                for (ItemStack itemstack : loottable.generateLootForPools(getEntityWorld().rand, lootcontext$builder.build())) {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    @Override
    public void setDead() {
    	super.setDead();
    	if(world.isRemote && isVisible()) {
    		spawnSmoke();
    		playSound(getDeathSound(), 0.1F + world.rand.nextFloat() * 0.9F,
    				0.1F + world.rand.nextFloat() * 0.9F);
    	}
    }
    
    @Override
    public boolean isMovementBlocked() {
    	return isFrozen() || getBuildupDuration() > 0;
    }
    
    @Override
    public void onLivingUpdate() {
    	super.onLivingUpdate();

        if(isVisible()) {
        	if(innerEntity != null) {
	        	innerEntity.isDead = isDead;
	        	innerEntity.deathTime = deathTime;
                innerEntity.setRevengeTarget(getAttackTarget());
	        	//innerEntity.lastAttackerTime = lastAttackerTime;
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
        	}
        	
        	if(world.isRemote) {
        		spawnSmoke();
        		if(isSwarm()) {
        			spawnSwarmParticles();
        		}
        	}
        }

        int buildupDuration = getBuildupDuration();
        if(buildupDuration > 0) setBuildupDuration(buildupDuration - 1);

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
	        	world.removeEntity(this);
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
            
            ParticleDarkSmoke particle = new ParticleDarkSmoke(world, particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
            if(this.isDead)
            	particle.setDeathParticles();
            particle.setLiving((float)getRemainingLife() / (float)REMAININGLIFE_MAX);
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void spawnSwarmParticles() {
    	int numParticles = 5 * (rand.nextInt((getSwarmTier() << 1) + 1) + 1);
    	for (int i=0; i < numParticles; i++) {            
            double particleX = posX - width /2 + width * rand.nextFloat();
            if(particleX < 0.7F && particleX >= 0) particleX += width /2;
            if(particleX > -0.7F && particleX <= 0) particleX -= width /2;
            double particleY = posY + height * rand.nextFloat();
            double particleZ = posZ - width / 2 + width * rand.nextFloat();
            
            float particleMotionX = (-0.5F + rand.nextFloat()) * 0.05F;
            float particleMotionY = (-0.5F + rand.nextFloat()) * 0.05F;
            float particleMotionZ = (-0.5F + rand.nextFloat()) * 0.05F;
            
            ParticleDegrade particle = new ParticleDegrade(world, particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
        }
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (soundIn != null && isVisible() && !this.isSilent()) {
            this.world.playSound(this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch, true);
        }
    }

    /**
     * If this entity is visible to the current player
     * @return If it is visible
     */
    public boolean isVisible() {
        return world.isRemote &&
    			(isAlternativelyVisible() || isClientVisible());
    }
    
    @SideOnly(Side.CLIENT)
    private boolean isClientVisible() {
    	if (isEnabledVengeance(Minecraft.getMinecraft().player)) {
            return true;
        }
        for (ItemStack itemStack : Minecraft.getMinecraft().player.getArmorInventoryList()) {
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof SpectralGlasses) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isAlternativelyVisible() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
		return player != null && player.capabilities.isCreativeMode;
	}

    @Override
    protected void collideWithNearbyEntities() {
        // Do nothing
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
        return isGlobalVengeance() || (player != null && ArrayUtils.contains(getVengeancePlayers(), player.getName()));
	}
    
    /**
     * Enable vengeance of this spirit for the given player.
     * @param player This player will be added to the target list.
     * @param enabled If vengeance should be enabled
     */
    public void setEnabledVengeance(EntityPlayer player, boolean enabled) {
    	String[] players = getVengeancePlayers();
        int index = ArrayUtils.indexOf(players, player.getName());
    	if(enabled && index == ArrayUtils.INDEX_NOT_FOUND)
    		players = ArrayUtils.add(players, player.getName());
    	else if(!enabled && index != ArrayUtils.INDEX_NOT_FOUND)
    		players = ArrayUtils.remove(players, index);
    	setVengeancePlayers(players);
    }

    public boolean isPlayer() {
        return containsPlayer();
    }

	/**
     * Get the inner entity.
     * @return inner entity
     */
    @SuppressWarnings("unchecked")
	public EntityLivingBase getInnerEntity() {
    	if(isSwarm()) {
    		this.height = getSwarmTier() / 2 + 1;
    		this.width = getSwarmTier() / 3 + 1;
    		return null;
    	}
    	if(innerEntity != null)
    		return innerEntity;
    	try {
			Class<EntityLivingBase> clazz = (Class<EntityLivingBase>) Class.forName(data.getInnerEntityName());
            if(!clazz.equals(VengeanceSpirit.class)) {
                ResourceLocation name = EntityList.getKey(clazz);
				Entity entity = EntityList.createEntityByIDFromName(name, world);
                if(canSustain((EntityLivingBase) entity)) {
                    innerEntity = (EntityLivingBase) entity;
                    this.setSize(innerEntity.width, innerEntity.height);
                    return innerEntity;
                }
			}
    	} catch (ClassNotFoundException e) {
    		// In this case it is a vengeance swarm.
		} catch (NullPointerException e) {
			EvilCraft.clog("Tried to spirit invalid entity, removing it now.", Level.ERROR);
 		} catch (ClassCastException e) {
			EvilCraft.clog("Tried to spirit invalid entity, removing it now.", Level.ERROR);
 		}
        if(!this.world.isRemote) {
            this.world.removeEntity(this);
        }
    	return null;
    }
    
    /**
     * Set the inner entity;
     * @param innerEntity inner entity
     */
    public void setInnerEntity(EntityLivingBase innerEntity) {
        if(innerEntity instanceof EntityPlayer) {
            setPlayerId(((EntityPlayer) innerEntity).getGameProfile().getId().toString());
            setPlayerName(((EntityPlayer) innerEntity).getGameProfile().getName());
            innerEntity = new EntityZombie(world);
        }
		this.data.setInnerEntityName(innerEntity.getClass().getName());
	}

	/**
     * If the given entity can be 'spiritted'
     * @param entityLiving The entity to check.
     * @return If it can become a spirit.
     */
	public static boolean canSustain(EntityLivingBase entityLiving) {
		for(Class<? extends EntityLivingBase> clazz : BLACKLIST) {
            if(clazz.isInstance(entityLiving)) {
				return false;
			}
		}
		return true;
	}

    /**
     * If the given entity class can be 'spiritted'
     * @param entityLivingClazz The entity class to check.
     * @return If it can become a spirit.
     */
    public static boolean canSustainClass(Class<?> entityLivingClazz) {
        for(Class<? extends EntityLivingBase> clazz : BLACKLIST) {
            if(clazz.equals(entityLivingClazz)) {
                return false;
            }
        }
        return true;
    }
	
	/**
     * Check if we can spawn a new vengeance spirit in the given location.
     * It will check if the amount of spirits in an area is below a certain threshold and if there aren't any gemstone
     * torches in the area
	 * @param world The world.
	 * @param blockPos The position.
     * @return If we are allowed to spawn a spirit.
     */
	@SuppressWarnings("unchecked")
	public static boolean canSpawnNew(World world, BlockPos blockPos) {
		int area = VengeanceSpiritConfig.spawnLimitArea;
		int threshold = VengeanceSpiritConfig.spawnLimit;
		AxisAlignedBB box = new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX(), blockPos.getY(), blockPos.getZ()).expand(area, area, area);
    	List<VengeanceSpirit> spirits = world.getEntitiesWithinAABB(VengeanceSpirit.class, box);
		if(spirits.size() >= threshold) {
            return false;
        }

        if(!Configs.isEnabled(GemStoneTorchConfig.class)) return true;

        return WorldHelpers.foldArea(world, GemStoneTorchConfig.area, blockPos,
                new WorldHelpers.WorldFoldingFunction<Boolean, Boolean>() {

            @Nullable
            @Override
            public Boolean apply(@Nullable Boolean input, World world, BlockPos blockPos) {
                return (input == null ||input) && world.getBlockState(blockPos).getBlock() != GemStoneTorchConfig._instance.getBlockInstance();
            }

        }, true);
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
		addFrozenDuration(world.rand.nextInt(4) + 3);
		if(world.isRemote) {
			showBurstParticles(hitX, hitY, hitZ, impactMotionX, impactMotionY, impactMotionZ);
		}
	}

	@SideOnly(Side.CLIENT)
	private void showBurstParticles(double hitX, double hitY, double hitZ, 
			double impactMotionX, double impactMotionY, double impactMotionZ) {
		for(int i = 0; i < world.rand.nextInt(5); i++) {
			float scale = 0.04F - rand.nextFloat() * 0.02F;
	    	float red = rand.nextFloat() * 0.2F + 0.3F;
	        float green = rand.nextFloat() * 0.2F + 0.3F;
	        float blue = rand.nextFloat() * 0.01F;
	        float ageMultiplier = (float) (rand.nextDouble() * 0.5D + 3D);
	        
	        double dx = 0.1D - rand.nextDouble() * 0.2D - impactMotionX * 0.1D;
	        double dy = 0.1D - rand.nextDouble() * 0.2D - impactMotionY * 0.1D;
	        double dz = 0.1D - rand.nextDouble() * 0.2D - impactMotionZ * 0.1D;
	        
			ParticleBlur blur = new ParticleBlur(world, hitX, hitY, hitZ, scale,
					dx, dy, dz, red, green, blue, ageMultiplier);
			Minecraft.getMinecraft().effectRenderer.addEffect(blur);
		}
	}

	/**
	 * Spawn a random vengeance spirit in the given area.
	 * @param world The world.
	 * @param blockPos The position.
	 * @param area The radius in which the spawn can occur.
	 * @return The spawned spirit, could be null.
	 */
	public static VengeanceSpirit spawnRandom(World world, BlockPos blockPos, int area) {
		VengeanceSpirit spirit = new VengeanceSpirit(world);
		int attempts = 50;
        int baseDistance = 5;
		while(canSpawnNew(world, blockPos) && attempts > 0) {
            BlockPos spawnPos = blockPos.add(
                    MathHelper.getInt(world.rand, baseDistance, baseDistance + area) * MathHelper.getInt(world.rand, -1, 1),
                    MathHelper.getInt(world.rand, 0, 3) * MathHelper.getInt(world.rand, -1, 1),
                    MathHelper.getInt(world.rand, baseDistance, baseDistance + area) * MathHelper.getInt(world.rand, -1, 1)
            );
            
            if(BlockHelpers.doesBlockHaveSolidTopSurface(world, spawnPos.add(0, -1, 0))) {
                spirit.setPosition((double) spawnPos.getX() + 0.5, (double) spawnPos.getY() + 0.5, (double) spawnPos.getZ() + 0.5);
                if(world.checkNoEntityCollision(spirit.getEntityBoundingBox())
                		&& spirit.isNotColliding()
                		&& !world.containsAnyLiquid(spirit.getEntityBoundingBox())) {
                    world.spawnEntity(spirit);
                    return spirit;
                }
            }
			attempts--;
		}
		return null;
	}

    @Override
	protected SoundEvent getDeathSound() {
		if(getInnerEntity() != null) {
			return ObfuscationHelpers.getDeathSound(getInnerEntity());
		}
		return EvilCraftSoundEvents.mob_vengeancespirit_death;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		EntityLivingBase entity = getInnerEntity();
		if(entity != null && entity instanceof EntityLiving) {
			return ObfuscationHelpers.getAmbientSound((EntityLiving) getInnerEntity());
		}
		return EvilCraftSoundEvents.mob_vengeancespirit_ambient;
	}

    @Optional.Method(modid = Reference.MOD_THAUMCRAFT)
    private void addWarp(EntityPlayer player) {
        if(GeneralConfig.thaumcraftVengeanceSpiritWarp) {
            // TODO: re-enable when thaumcraft is updated
            //ThaumcraftApiHelper.addWarpToPlayer(player, 1, true);
        }
    }

	/**
	 * Add an entity class to the blacklist, every subinstance of this class will not
	 * be spirited anymore.
	 * @param clazz The root class that will be blocked from spiritation.
	 */
	public static void addToBlacklist(Class<? extends EntityLivingBase> clazz) {
		if(BLACKLIST.add(clazz))
			EvilCraft.clog("Added entity class " + clazz.getCanonicalName()
                    + " to the spirit blacklist.", Level.TRACE);
	}

    /**
     * Add an entity class to the blacklist, every subinstance of this class will not
     * be spirited anymore.
     * This should only be called by IMC message handlers.
     * @param clazz The root class that will be blocked from spiritation.
     */
    public static void addToBlacklistIMC(Class<? extends EntityLivingBase> clazz) {
        IMC_BLACKLIST.add(clazz);
        addToBlacklist(clazz);
    }
	
	@SuppressWarnings("unchecked")
	protected static void setBlacklist(String[] blacklist) {
        BLACKLIST.clear();
		for(String entity : blacklist) {
			Class<EntityLivingBase> clazz = (Class<EntityLivingBase>) EntityList.getClass(new ResourceLocation(entity));
			if(clazz == null) {
				EvilCraft.clog("Skipped adding entity by name '" + entity
                        + "' to the spirit blacklist, because it could not be found.", Level.INFO);
			} else {
				addToBlacklist(clazz);
			}
		}

        for(Class<? extends EntityLivingBase> clazz : IMC_BLACKLIST) {
            addToBlacklist(clazz);
        }
		
		// Hard-code some entities
		addToBlacklist(VengeanceSpirit.class);
        addToBlacklist(ControlledZombie.class);
    	addToBlacklist(EntityDragon.class);
	}
	
	/**
	 * The changed callback for the spirit blacklist.
	 * @author rubensworks
	 *
	 */
	public static class SpiritBlacklistChanged implements IChangedCallback {

		private static boolean calledOnce = false;
		
		@Override
		public void onChanged(Object value) {
            if(calledOnce) {
				setBlacklist((String[]) value);
			}
			calledOnce = true;
		}
		
		@Override
		public void onRegisteredPostInit(Object value) {
			onChanged(value);
		}
		
	}

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }

    @Override
    public boolean handleWaterMovement() {
        // Ignore water movement and particles
        return this.inWater;
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    public static VengeanceSpirit fromNBT(World world, NBTTagCompound spiritTag) {
        VengeanceSpirit spirit = new VengeanceSpirit(world);
        spirit.readEntityFromNBT(spiritTag);
        return spirit;
    }
}
