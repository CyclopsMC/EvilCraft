package org.cyclops.evilcraft.entity.monster;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.experimental.Delegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;
import org.cyclops.cyclopscore.inventory.PlayerInventoryIterator;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.ExtendedDamageSource;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockGemStoneTorchConfig;
import org.cyclops.evilcraft.client.particle.ParticleDarkSmokeData;
import org.cyclops.evilcraft.core.monster.EntityNoMob;
import org.cyclops.evilcraft.item.ItemBurningGemStone;
import org.cyclops.evilcraft.item.ItemSpectralGlasses;
import org.cyclops.evilcraft.item.ItemVengeanceRing;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * A silverfish for the nether.
 * @author rubensworks
 *
 */
public class EntityVengeanceSpirit extends EntityNoMob {

    static {
        MinecraftForge.EVENT_BUS.register(EntityVengeanceSpirit.class);
    }

    private static final Set<String> IMC_BLACKLIST = Sets.newHashSet();
    
    /**
     * The minimum life duration in ticks the spirits should have.
     */
    public static final int REMAININGLIFE_MIN = 250;
    /**
     * The maximum life duration in ticks the spirits should have.
     */
    public static final int REMAININGLIFE_MAX = 1000;

    public static final DataParameter<String> WATCHERID_INNER = EntityDataManager.<String>defineId(EntityVengeanceSpirit.class, DataSerializers.STRING);
    public static final DataParameter<Integer> WATCHERID_REMAININGLIFE = EntityDataManager.<Integer>defineId(EntityVengeanceSpirit.class, DataSerializers.INT);
    public static final DataParameter<Integer> WATCHERID_FROZENDURATION = EntityDataManager.<Integer>defineId(EntityVengeanceSpirit.class, DataSerializers.INT);
    public static final DataParameter<Integer> WATCHERID_GLOBALVENGEANCE = EntityDataManager.<Integer>defineId(EntityVengeanceSpirit.class, DataSerializers.INT);
    public static final DataParameter<String> WATCHERID_VENGEANCEPLAYERS = EntityDataManager.<String>defineId(EntityVengeanceSpirit.class, DataSerializers.STRING);
    public static final DataParameter<Integer> WATCHERID_ISSWARM = EntityDataManager.<Integer>defineId(EntityVengeanceSpirit.class, DataSerializers.INT);
    public static final DataParameter<Integer> WATCHERID_SWARMTIER = EntityDataManager.<Integer>defineId(EntityVengeanceSpirit.class, DataSerializers.INT);
    public static final DataParameter<Integer> WATCHERID_BUILDUP = EntityDataManager.<Integer>defineId(EntityVengeanceSpirit.class, DataSerializers.INT);
    public static final DataParameter<String> WATCHERID_PLAYERID = EntityDataManager.<String>defineId(EntityVengeanceSpirit.class, DataSerializers.STRING);
    public static final DataParameter<String> WATCHERID_PLAYERNAME = EntityDataManager.<String>defineId(EntityVengeanceSpirit.class, DataSerializers.STRING);

    @Getter
    @Delegate
    private EntityVengeanceSpiritSyncedData data;

	private MobEntity innerEntity = null;

	@Nullable
    private EntityType<?> preferredInnerEntity;

    private final Set<ServerPlayerEntity> entanglingPlayers = Sets.newHashSet();

    public EntityVengeanceSpirit(EntityType<? extends EntityVengeanceSpirit> type, World level) {
        this(type, level, null);
    }

    public EntityVengeanceSpirit(World level) {
        this(RegistryEntries.ENTITY_VENGEANCE_SPIRIT, level, null);
    }

    public EntityVengeanceSpirit(EntityType<? extends EntityVengeanceSpirit> type, World level, @Nullable EntityType<?> preferredInnerEntity) {
        super(type, level);
        this.preferredInnerEntity = preferredInnerEntity;

        this.maxUpStep = 5.0F;
        this.blocksBuilding = false;

        float damage = 0.5F;
        int remainingLife = MathHelper.nextInt(level.random, REMAININGLIFE_MIN, REMAININGLIFE_MAX);
        if(isSwarm()) {
        	damage += 0.5D * getSwarmTier();
        	remainingLife += (REMAININGLIFE_MAX - REMAININGLIFE_MIN) * getSwarmTier();
        }

        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(2, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, damage));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, true));

        setRemainingLife(remainingLife);
        setFrozenDuration(0);
    }

    @Override
    public double getAttributeValue(Attribute attribute) {
        if (attribute == Attributes.MOVEMENT_SPEED) {
            double speed = 0.25D;
            if(isSwarm()) {
                speed += 0.125D * getSwarmTier();
            }
            return speed;
        }
        return super.getAttributeValue(attribute);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void vengeanceEvent(LivingDeathEvent event) {
        if (event.getEntityLiving() != null) {
            World level = event.getEntityLiving().level;
            boolean directToPlayer = shouldDirectSpiritToPlayer(event);
            if (!level.isClientSide()
                    && level.getDifficulty() != Difficulty.PEACEFUL
                    && EntityVengeanceSpirit.canSustain(event.getEntityLiving())
                    && (directToPlayer || EntityVengeanceSpirit.canSpawnNew(level, event.getEntityLiving().blockPosition()))) {
                EntityVengeanceSpirit spirit = new EntityVengeanceSpirit(level);
                spirit.setInnerEntity(event.getEntityLiving());
                spirit.copyPosition(event.getEntityLiving());
                level.addFreshEntity(spirit);
                if(directToPlayer) {
                    PlayerEntity player = (PlayerEntity) event.getSource().getDirectEntity();
                    spirit.setBuildupDuration(3 * MinecraftHelpers.SECOND_IN_TICKS);
                    spirit.setGlobalVengeance(true);
                    spirit.setTarget(player);
                }
            }
        }
    }

    private static boolean shouldDirectSpiritToPlayer(LivingDeathEvent event) {
        if(event.getSource().getDirectEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getDirectEntity();
            for(PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player); it.hasNext();) {
                ItemStack itemStack = it.next();
                if(!itemStack.isEmpty() && itemStack.getItem() instanceof ItemVengeanceRing) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
	public void defineSynchedData() {
        super.defineSynchedData();
        if (preferredInnerEntity == null)
            data = new EntityVengeanceSpiritSyncedData(this.entityData, EntityVengeanceSpiritData.getRandomInnerEntity(this.random));
        else
            data = new EntityVengeanceSpiritSyncedData(this.entityData, preferredInnerEntity);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        data.writeNBT(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        data.readNBT(tag);
    }

    @Override
    public ResourceLocation getDefaultLootTable() {
        return new ResourceLocation(Reference.MOD_ID, "entities/" + getType().getRegistryName().getPath());
    }

    @Override
    protected float getVoicePitch() {
        return super.getVoicePitch() / 3.0F;
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return !(damageSource instanceof ExtendedDamageSource.VengeanceBeamDamageSource || damageSource == DamageSource.OUT_OF_WORLD);
    }
    
    @Override
    public boolean doHurtTarget(Entity entity) {
        if(getBuildupDuration() > 0) return false; // Don't attack anything when still building up.

        this.remove();
    	if(entity instanceof PlayerEntity) {
    		PlayerEntity player = (PlayerEntity) entity;

    		if(ItemBurningGemStone.damageForPlayer(player, isSwarm() ? getSwarmTier() : 0, false)) {
    			entity.setDeltaMovement(
    					(double)(-MathHelper.sin(this.yRot * (float)Math.PI / 180.0F) * 0.01F),
    					0.025D,
    					(double)(MathHelper.cos(this.yRot * (float)Math.PI / 180.0F) * 0.01F));
    			entity.hurt(DamageSource.mobAttack(this), 0.1F);
    			return false;
    		}
    	}
        return super.doHurtTarget(entity);
    }

    @Override
    protected void dropFromLootTable(DamageSource damageSource, boolean fromPlayer) {
        super.dropFromLootTable(damageSource, fromPlayer);

        // Also drop loot from inner entity!
        LivingEntity innerEntity = getInnerEntity();
        if (innerEntity != null && damageSource != DamageSource.OUT_OF_WORLD) {
            ResourceLocation deathLootTable = innerEntity.getLootTable();
            if (deathLootTable != null) {
                LootTable loottable = level.getServer().getLootTables().get(deathLootTable);
                LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) level))
                        .withRandom(this.random)
                        .withParameter(LootParameters.THIS_ENTITY, innerEntity)
                        .withParameter(LootParameters.ORIGIN, new Vector3d(getX(), getY(), getZ()))
                        .withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.GENERIC)
                        .withOptionalParameter(LootParameters.KILLER_ENTITY, null)
                        .withOptionalParameter(LootParameters.DIRECT_KILLER_ENTITY, null);

                if (fromPlayer && this.lastHurtByPlayer != null) {
                    lootcontext$builder = lootcontext$builder
                            .withParameter(LootParameters.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer)
                            .withLuck(this.lastHurtByPlayer.getLuck());
                }

                for (ItemStack itemstack : loottable.getRandomItems(lootcontext$builder.create(LootParameterSets.ENTITY))) {
                    this.spawnAtLocation(itemstack, 0.0F);
                }
            }
        }
    }

    @Override
    public void remove() {
    	super.remove();
    	if(level.isClientSide() && isVisible()) {
    		spawnSmoke();
    		playSound(getDeathSound(), 0.1F + level.random.nextFloat() * 0.9F,
    				0.1F + level.random.nextFloat() * 0.9F);
    	}
    }
    
    @Override
    public boolean isImmobile() {
    	return isFrozen() || getBuildupDuration() > 0;
    }
    
    @Override
    public void aiStep() {
    	super.aiStep();

        if(isVisible()) {
        	if(innerEntity != null) {
	        	innerEntity.deathTime = deathTime;
                innerEntity.setTarget(getTarget());
	        	//innerEntity.lastAttackerTime = lastAttackerTime;
	        	innerEntity.hurtTime = hurtTime;
	        	innerEntity.xRot = xRot;
	        	innerEntity.yRot = yRot;
	        	innerEntity.xRotO = xRotO;
	        	innerEntity.yRotO = yRotO;
	        	innerEntity.yBodyRot = yBodyRot;
	        	innerEntity.yBodyRotO = yBodyRotO;
	        	innerEntity.yHeadRot = yHeadRot;
	        	innerEntity.yHeadRotO = yHeadRotO;
        	}
        	
        	if(level.isClientSide()) {
        		spawnSmoke();
        		if(isSwarm()) {
        			spawnSwarmParticles();
        		}
        	}
        }

        int buildupDuration = getBuildupDuration();
        if(buildupDuration > 0) setBuildupDuration(buildupDuration - 1);

        if(isFrozen()) {
            this.setDeltaMovement(0, 0, 0);
        	addFrozenDuration(-1);
        } else {
            setRemainingLife(getRemainingLife() - 1);
	        if(getRemainingLife() <= 0) {
	        	this.remove();
	        }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnSmoke() {
        EntitySize size = getDimensions(getPose());
    	int numParticles = random.nextInt(5);
    	if(!this.isAlive())
    		numParticles *= 10;
    	float clearRange = size.width; // Particles can't spawn within this X and Z distance
    	for (int i=0; i < numParticles; i++) {            
            double particleX = getX() - size.width /2 + size.width * random.nextFloat();
            if(particleX < 0.7F && particleX >= 0) particleX += size.width /2;
            if(particleX > -0.7F && particleX <= 0) particleX -= size.width /2;
            double particleY = getY() + size.height * random.nextFloat();
            double particleZ = getZ() - size.width / 2 + size.width * random.nextFloat();
            if(particleZ < clearRange && particleZ >= 0) particleZ += size.width /2;
            if(particleZ > -clearRange && particleZ <= 0) particleZ -= size.width /2;
            
            float particleMotionX = (-0.5F + random.nextFloat()) * 0.05F;
            float particleMotionY = (-0.5F + random.nextFloat()) * 0.05F;
            float particleMotionZ = (-0.5F + random.nextFloat()) * 0.05F;

            Minecraft.getInstance().levelRenderer.addParticle(
                    new ParticleDarkSmokeData(!this.isAlive()), false,
                    particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnSwarmParticles() {
        EntitySize size = getDimensions(getPose());
    	int numParticles = 5 * (random.nextInt((getSwarmTier() << 1) + 1) + 1);
    	for (int i=0; i < numParticles; i++) {            
            double particleX = getX() - size.width /2 + size.width * random.nextFloat();
            if(particleX < 0.7F && particleX >= 0) particleX += size.width /2;
            if(particleX > -0.7F && particleX <= 0) particleX -= size.width /2;
            double particleY = getY() + size.height * random.nextFloat();
            double particleZ = getZ() - size.width / 2 + size.width * random.nextFloat();
            
            float particleMotionX = (-0.5F + random.nextFloat()) * 0.05F;
            float particleMotionY = (-0.5F + random.nextFloat()) * 0.05F;
            float particleMotionZ = (-0.5F + random.nextFloat()) * 0.05F;

            Minecraft.getInstance().levelRenderer.addParticle(
                    RegistryEntries.PARTICLE_DEGRADE, false,
                    particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
        }
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (soundIn != null && isVisible() && !this.isSilent()) {
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), soundIn, this.getSoundSource(), volume, pitch, true);
        }
    }

    /**
     * If this entity is visible to the current player
     * @return If it is visible
     */
    public boolean isVisible() {
        return level.isClientSide() &&
    			(isAlternativelyVisible() || isClientVisible());
    }
    
    @OnlyIn(Dist.CLIENT)
    private boolean isClientVisible() {
    	if (isEnabledVengeance(Minecraft.getInstance().player)) {
            return true;
        }
        PlayerInventoryIterator it = new PlayerInventoryIterator(Minecraft.getInstance().player);
    	while (it.hasNext()) {
    	    ItemStack itemStack = it.next();
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemSpectralGlasses) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isAlternativelyVisible() {
        ClientPlayerEntity player = Minecraft.getInstance().player;
		return EntityVengeanceSpiritConfig.alwaysVisibleInCreative && player != null && player.isCreative();
	}

    @Override
    protected void pushEntities() {
        // Do nothing
    }

    @Override
    public boolean isPushable() {
        return false;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    // MCP: onStruckByLightning
    @Override
    public void thunderHit(ServerWorld level, LightningBoltEntity lightning) {
    	setGlobalVengeance(true);
    }
    
    @Override
    public boolean canSee(Entity entity) {
    	if(entity instanceof PlayerEntity)
    		return isEnabledVengeance((PlayerEntity) entity);
    	else
    		return super.canSee(entity);
    }
    
    /**
     * If the given player is vengeanced by this spirit
     * @param player the player.
     * @return If it should be visible.
     */
    public boolean isEnabledVengeance(PlayerEntity player) {
        return isGlobalVengeance() || (player != null && ArrayUtils.contains(getVengeancePlayers(), player.getName()));
	}
    
    /**
     * Enable vengeance of this spirit for the given player.
     * @param player This player will be added to the target list.
     * @param enabled If vengeance should be enabled
     */
    public void setEnabledVengeance(PlayerEntity player, boolean enabled) {
    	String[] players = getVengeancePlayers();
        int index = ArrayUtils.indexOf(players, player.getDisplayName().getString());
    	if(enabled && index == ArrayUtils.INDEX_NOT_FOUND)
    		players = ArrayUtils.add(players, player.getName().getString());
    	else if(!enabled && index != ArrayUtils.INDEX_NOT_FOUND)
    		players = ArrayUtils.remove(players, index);
    	setVengeancePlayers(players);
    }

    public boolean isPlayer() {
        return containsPlayer();
    }

    @Override
    public EntitySize getDimensions(Pose poseIn) {
        if(isSwarm()) {
            return EntitySize.scalable(getSwarmTier() / 3 + 1, getSwarmTier() / 2 + 1);
        }
        MobEntity innerEntity = getInnerEntity();
        if (innerEntity != null) {
            return innerEntity.getDimensions(poseIn);
        }
        return super.getDimensions(poseIn);
    }

    /**
     * Get the inner entity.
     * @return inner entity, null if it is a swarm
     */
    @Nullable
	public MobEntity getInnerEntity() {
    	if(isSwarm()) {
    		return null;
    	}
        EntityType<?> entityType = data.getInnerEntityType();
    	if(innerEntity != null) {
    	    if (entityType != null && entityType == innerEntity.getType()) {
                return innerEntity;
            }
        }
    	try {
            if (entityType != RegistryEntries.ENTITY_VENGEANCE_SPIRIT) {
                Entity entity = entityType.create(level);
                if (canSustain((MobEntity) entity)) {
                    return innerEntity = (MobEntity) entity;
                }
            }
		} catch (NullPointerException | ClassCastException e) {
			// If we have an invalid entity, spirit becomes a swarm
 		}
    	return null;
    }
    
    /**
     * Set the inner entity;
     * @param innerEntity inner entity
     */
    public void setInnerEntity(LivingEntity innerEntity) {
        if(innerEntity instanceof PlayerEntity) {
            setPlayerId(((PlayerEntity) innerEntity).getGameProfile().getId().toString());
            setPlayerName(((PlayerEntity) innerEntity).getGameProfile().getName());
            this.data.setInnerEntityType(EntityType.ZOMBIE);
        } else {
            this.data.setInnerEntityType(innerEntity.getType());
        }
	}

	/**
     * If the given entity can be 'spiritted'
     * @param entityLiving The entity to check.
     * @return If it can become a spirit.
     */
	public static boolean canSustain(LivingEntity entityLiving) {
        String entityName = entityLiving.getType().getRegistryName().toString();
        for (String blacklistedRegex : EntityVengeanceSpiritConfig.entityBlacklist) {
            if (entityName.matches(blacklistedRegex)) {
                return false;
            }
        }
        for (String blacklistedRegex : IMC_BLACKLIST) {
            if (entityName.matches(blacklistedRegex)) {
                return false;
            }
        }
		return true;
	}
	
	/**
     * Check if we can spawn a new vengeance spirit in the given location.
     * It will check if the amount of spirits in an area is below a certain threshold and if there aren't any gemstone
     * torches in the area
	 * @param level The level.
	 * @param blockPos The position.
     * @return If we are allowed to spawn a spirit.
     */
	@SuppressWarnings("unchecked")
	public static boolean canSpawnNew(World level, BlockPos blockPos) {
		int area = EntityVengeanceSpiritConfig.spawnLimitArea;
		int threshold = EntityVengeanceSpiritConfig.spawnLimit;
		AxisAlignedBB box = new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX(), blockPos.getY(), blockPos.getZ()).inflate(area, area, area);
    	List<EntityVengeanceSpirit> spirits = level.getEntitiesOfClass(EntityVengeanceSpirit.class, box);
		if(spirits.size() >= threshold) {
            return false;
        }

        return WorldHelpers.foldArea(level, BlockGemStoneTorchConfig.area, blockPos,
                (input, level1, blockPos1) -> input
                        && !BlockTags.getAllTags().getTag(new ResourceLocation("evilcraft:vengeance_spirit_blocker")).contains(level1.getBlockState(blockPos1).getBlock()), true);
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
		addFrozenDuration(level.random.nextInt(4) + 3);
		if(level.isClientSide()) {
			showBurstParticles(hitX, hitY, hitZ, impactMotionX, impactMotionY, impactMotionZ);
		}
	}

	@OnlyIn(Dist.CLIENT)
	private void showBurstParticles(double hitX, double hitY, double hitZ, 
			double impactMotionX, double impactMotionY, double impactMotionZ) {
		for(int i = 0; i < level.random.nextInt(5); i++) {
			float scale = 0.04F - random.nextFloat() * 0.02F;
	    	float red = random.nextFloat() * 0.2F + 0.3F;
	        float green = random.nextFloat() * 0.2F + 0.3F;
	        float blue = random.nextFloat() * 0.01F;
	        float ageMultiplier = (float) (random.nextDouble() * 0.5D + 3D);
	        
	        double dx = 0.1D - random.nextDouble() * 0.2D - impactMotionX * 0.1D;
	        double dy = 0.1D - random.nextDouble() * 0.2D - impactMotionY * 0.1D;
	        double dz = 0.1D - random.nextDouble() * 0.2D - impactMotionZ * 0.1D;

            Minecraft.getInstance().levelRenderer.addParticle(
                    new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                    hitX, hitY, hitZ, dx, dy, dz);
		}
	}

	/**
	 * Spawn a random vengeance spirit in the given area.
	 * @param level The level.
	 * @param blockPos The position.
	 * @param area The radius in which the spawn can occur.
	 * @return The spawned spirit, could be null.
	 */
	public static EntityVengeanceSpirit spawnRandom(World level, BlockPos blockPos, int area) {
		EntityVengeanceSpirit spirit = new EntityVengeanceSpirit(level);
		int attempts = 50;
        int baseDistance = 5;
		while(canSpawnNew(level, blockPos) && attempts > 0) {
            BlockPos spawnPos = blockPos.offset(
                    MathHelper.nextInt(level.random, baseDistance, baseDistance + area) * MathHelper.nextInt(level.random, -1, 1),
                    MathHelper.nextInt(level.random, 0, 3) * MathHelper.nextInt(level.random, -1, 1),
                    MathHelper.nextInt(level.random, baseDistance, baseDistance + area) * MathHelper.nextInt(level.random, -1, 1)
            );
            
            if(BlockHelpers.doesBlockHaveSolidTopSurface(level, spawnPos.offset(0, -1, 0))) {
                spirit.setPos((double) spawnPos.getX() + 0.5, (double) spawnPos.getY() + 0.5, (double) spawnPos.getZ() + 0.5);
                if(!level.noCollision(spirit)
                		&& !level.containsAnyLiquid(spirit.getBoundingBox())) {
                    level.addFreshEntity(spirit);
                    return spirit;
                }
            }
			attempts--;
		}
		return null;
	}

    @Override
    public SoundEvent getDeathSound() {
		if(getInnerEntity() != null) {
            return getInnerEntity().getDeathSound();
		}
		return EvilCraftSoundEvents.mob_vengeancespirit_death;
	}
	
	@Override
    public SoundEvent getAmbientSound() {
		LivingEntity entity = getInnerEntity();
		if(entity != null && entity instanceof MobEntity) {
            return ((MobEntity) getInnerEntity()).getAmbientSound();
		}
		return EvilCraftSoundEvents.mob_vengeancespirit_ambient;
	}

    /**
     * Add an entity name to the blacklist, every subinstance of this class will not
     * be spirited anymore.
     * This should only be called by IMC message handlers.
     * @param entityName The entity name or regexthat will be blocked from spiritation.
     */
    public static void addToBlacklistIMC(String entityName) {
        IMC_BLACKLIST.add(entityName);
        EvilCraft.clog("Added entity name " + entityName + " to the spirit blacklist.", Level.TRACE);
    }

    @Override
    public boolean updateFluidHeightAndDoFluidPushing(ITag<Fluid> fluidTag, double p_210500_2_) {
        // Ignore water movement and particles
        return this.wasTouchingWater;
    }

    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    public static EntityVengeanceSpirit fromNBT(World level, CompoundNBT spiritTag) {
        EntityVengeanceSpirit spirit = new EntityVengeanceSpirit(level);
        spirit.readAdditionalSaveData(spiritTag);
        return spirit;
    }

    public void addEntanglingPlayer(ServerPlayerEntity player) {
        this.entanglingPlayers.add(player);
    }

    public Set<ServerPlayerEntity> getEntanglingPlayers() {
        return entanglingPlayers;
    }
}
