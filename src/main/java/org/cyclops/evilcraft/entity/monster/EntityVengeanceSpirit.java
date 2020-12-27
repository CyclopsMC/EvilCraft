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

    public static final DataParameter<String> WATCHERID_INNER = EntityDataManager.<String>createKey(EntityVengeanceSpirit.class, DataSerializers.STRING);
    public static final DataParameter<Integer> WATCHERID_REMAININGLIFE = EntityDataManager.<Integer>createKey(EntityVengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> WATCHERID_FROZENDURATION = EntityDataManager.<Integer>createKey(EntityVengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> WATCHERID_GLOBALVENGEANCE = EntityDataManager.<Integer>createKey(EntityVengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<String> WATCHERID_VENGEANCEPLAYERS = EntityDataManager.<String>createKey(EntityVengeanceSpirit.class, DataSerializers.STRING);
    public static final DataParameter<Integer> WATCHERID_ISSWARM = EntityDataManager.<Integer>createKey(EntityVengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> WATCHERID_SWARMTIER = EntityDataManager.<Integer>createKey(EntityVengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> WATCHERID_BUILDUP = EntityDataManager.<Integer>createKey(EntityVengeanceSpirit.class, DataSerializers.VARINT);
    public static final DataParameter<String> WATCHERID_PLAYERID = EntityDataManager.<String>createKey(EntityVengeanceSpirit.class, DataSerializers.STRING);
    public static final DataParameter<String> WATCHERID_PLAYERNAME = EntityDataManager.<String>createKey(EntityVengeanceSpirit.class, DataSerializers.STRING);

    @Getter
    @Delegate
    private EntityVengeanceSpiritSyncedData data;

	private MobEntity innerEntity = null;

	@Nullable
    private EntityType<?> preferredInnerEntity;

    private final Set<ServerPlayerEntity> entanglingPlayers = Sets.newHashSet();

    public EntityVengeanceSpirit(EntityType<? extends EntityVengeanceSpirit> type, World world) {
        this(type, world, null);
    }

    public EntityVengeanceSpirit(World world) {
        this(RegistryEntries.ENTITY_VENGEANCE_SPIRIT, world, null);
    }

    public EntityVengeanceSpirit(EntityType<? extends EntityVengeanceSpirit> type, World world, @Nullable EntityType<?> preferredInnerEntity) {
        super(type, world);
        this.preferredInnerEntity = preferredInnerEntity;

        this.stepHeight = 5.0F;
        this.preventEntitySpawning = false;

        float damage = 0.5F;
        int remainingLife = MathHelper.nextInt(world.rand, REMAININGLIFE_MIN, REMAININGLIFE_MAX);
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
    public double getBaseAttributeValue(Attribute attribute) {
        if (attribute == Attributes.MOVEMENT_SPEED) {
            double speed = 0.25D;
            if(isSwarm()) {
                speed += 0.125D * getSwarmTier();
            }
            return speed;
        }
        return super.getBaseAttributeValue(attribute);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void vengeanceEvent(LivingDeathEvent event) {
        if (event.getEntityLiving() != null) {
            World world = event.getEntityLiving().world;
            boolean directToPlayer = shouldDirectSpiritToPlayer(event);
            if (!world.isRemote()
                    && world.getDifficulty() != Difficulty.PEACEFUL
                    && EntityVengeanceSpirit.canSustain(event.getEntityLiving())
                    && (directToPlayer || EntityVengeanceSpirit.canSpawnNew(world, event.getEntityLiving().getPosition()))) {
                EntityVengeanceSpirit spirit = new EntityVengeanceSpirit(world);
                spirit.setInnerEntity(event.getEntityLiving());
                spirit.copyLocationAndAnglesFrom(event.getEntityLiving());
                world.addEntity(spirit);
                if(directToPlayer) {
                    PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
                    spirit.setBuildupDuration(3 * MinecraftHelpers.SECOND_IN_TICKS);
                    spirit.setGlobalVengeance(true);
                    spirit.setAttackTarget(player);
                }
            }
        }
    }

    private static boolean shouldDirectSpiritToPlayer(LivingDeathEvent event) {
        if(event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
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
	public void registerData() {
        super.registerData();
        if (preferredInnerEntity == null)
            data = new EntityVengeanceSpiritSyncedData(this.dataManager, EntityVengeanceSpiritData.getRandomInnerEntity(this.rand));
        else
            data = new EntityVengeanceSpiritSyncedData(this.dataManager, preferredInnerEntity);
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        data.writeNBT(tag);
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        data.readNBT(tag);
    }

    @Override
    public ResourceLocation getLootTable() {
        return new ResourceLocation(Reference.MOD_ID, "entities/" + getType().getRegistryName().getPath());
    }
    
    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() / 3.0F;
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return !(damageSource instanceof ExtendedDamageSource.VengeanceBeamDamageSource || damageSource == DamageSource.OUT_OF_WORLD);
    }
    
    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if(getBuildupDuration() > 0) return false; // Don't attack anything when still building up.

        this.remove();
    	if(entity instanceof PlayerEntity) {
    		PlayerEntity player = (PlayerEntity) entity;

    		if(ItemBurningGemStone.damageForPlayer(player, isSwarm() ? getSwarmTier() : 0, false)) {
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
    protected void dropLoot(DamageSource damageSource, boolean fromPlayer) {
        super.dropLoot(damageSource, fromPlayer);

        // Also drop loot from inner entity!
        LivingEntity innerEntity = getInnerEntity();
        if (innerEntity instanceof MobEntity && damageSource != DamageSource.OUT_OF_WORLD) {
            ResourceLocation deathLootTable = ((MobEntity) innerEntity).getLootTable();
            if (deathLootTable != null) {
                LootTable loottable = getEntityWorld().getServer().getLootTableManager().getLootTableFromLocation(deathLootTable);
                LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) getEntityWorld()))
                        .withRandom(this.rand)
                        .withParameter(LootParameters.THIS_ENTITY, innerEntity)
                        .withParameter(LootParameters.field_237457_g_, new Vector3d(getPosX(), getPosY(), getPosZ()))
                        .withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.GENERIC)
                        .withNullableParameter(LootParameters.KILLER_ENTITY, null)
                        .withNullableParameter(LootParameters.DIRECT_KILLER_ENTITY, null);

                if (fromPlayer && this.attackingPlayer != null) {
                    lootcontext$builder = lootcontext$builder
                            .withParameter(LootParameters.LAST_DAMAGE_PLAYER, this.attackingPlayer)
                            .withLuck(this.attackingPlayer.getLuck());
                }

                for (ItemStack itemstack : loottable.generate(lootcontext$builder.build(LootParameterSets.ENTITY))) {
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }
    }

    @Override
    public void remove() {
    	super.remove();
    	if(world.isRemote() && isVisible()) {
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
    public void livingTick() {
    	super.livingTick();

        if(isVisible()) {
        	if(innerEntity != null) {
	        	innerEntity.deathTime = deathTime;
                innerEntity.setRevengeTarget(getAttackTarget());
	        	//innerEntity.lastAttackerTime = lastAttackerTime;
	        	innerEntity.hurtTime = hurtTime;
	        	innerEntity.rotationPitch = rotationPitch;
	        	innerEntity.rotationYaw = rotationYaw;
	        	innerEntity.rotationYawHead = rotationYawHead;
	        	innerEntity.renderYawOffset = renderYawOffset;
	        	innerEntity.prevRotationPitch = prevRotationPitch;
	        	innerEntity.prevRenderYawOffset = prevRenderYawOffset;
	        	innerEntity.prevRotationYaw = prevRotationYaw;
	        	innerEntity.prevRotationYawHead = prevRotationYawHead;
        	}
        	
        	if(world.isRemote()) {
        		spawnSmoke();
        		if(isSwarm()) {
        			spawnSwarmParticles();
        		}
        	}
        }

        int buildupDuration = getBuildupDuration();
        if(buildupDuration > 0) setBuildupDuration(buildupDuration - 1);

        if(isFrozen()) {
            this.setMotion(0, 0, 0);
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
        EntitySize size = getSize(getPose());
    	int numParticles = rand.nextInt(5);
    	if(!this.isAlive())
    		numParticles *= 10;
    	float clearRange = size.width; // Particles can't spawn within this X and Z distance
    	for (int i=0; i < numParticles; i++) {            
            double particleX = getPosX() - size.width /2 + size.width * rand.nextFloat();
            if(particleX < 0.7F && particleX >= 0) particleX += size.width /2;
            if(particleX > -0.7F && particleX <= 0) particleX -= size.width /2;
            double particleY = getPosY() + size.height * rand.nextFloat();
            double particleZ = getPosZ() - size.width / 2 + size.width * rand.nextFloat();
            if(particleZ < clearRange && particleZ >= 0) particleZ += size.width /2;
            if(particleZ > -clearRange && particleZ <= 0) particleZ -= size.width /2;
            
            float particleMotionX = (-0.5F + rand.nextFloat()) * 0.05F;
            float particleMotionY = (-0.5F + rand.nextFloat()) * 0.05F;
            float particleMotionZ = (-0.5F + rand.nextFloat()) * 0.05F;

            Minecraft.getInstance().worldRenderer.addParticle(
                    new ParticleDarkSmokeData(!this.isAlive()), false,
                    particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnSwarmParticles() {
        EntitySize size = getSize(getPose());
    	int numParticles = 5 * (rand.nextInt((getSwarmTier() << 1) + 1) + 1);
    	for (int i=0; i < numParticles; i++) {            
            double particleX = getPosX() - size.width /2 + size.width * rand.nextFloat();
            if(particleX < 0.7F && particleX >= 0) particleX += size.width /2;
            if(particleX > -0.7F && particleX <= 0) particleX -= size.width /2;
            double particleY = getPosY() + size.height * rand.nextFloat();
            double particleZ = getPosZ() - size.width / 2 + size.width * rand.nextFloat();
            
            float particleMotionX = (-0.5F + rand.nextFloat()) * 0.05F;
            float particleMotionY = (-0.5F + rand.nextFloat()) * 0.05F;
            float particleMotionZ = (-0.5F + rand.nextFloat()) * 0.05F;

            Minecraft.getInstance().worldRenderer.addParticle(
                    RegistryEntries.PARTICLE_DEGRADE, false,
                    particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
        }
    }

    @Override
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (soundIn != null && isVisible() && !this.isSilent()) {
            this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), soundIn, this.getSoundCategory(), volume, pitch, true);
        }
    }

    /**
     * If this entity is visible to the current player
     * @return If it is visible
     */
    public boolean isVisible() {
        return world.isRemote() &&
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

    // MCP: onStruckByLightning
    @Override
    public void func_241841_a(ServerWorld world, LightningBoltEntity lightning) {
    	setGlobalVengeance(true);
    }
    
    @Override
    public boolean canEntityBeSeen(Entity entity) {
    	if(entity instanceof PlayerEntity)
    		return isEnabledVengeance((PlayerEntity) entity);
    	else
    		return super.canEntityBeSeen(entity);
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
    public EntitySize getSize(Pose poseIn) {
        if(isSwarm()) {
            return EntitySize.flexible(getSwarmTier() / 3 + 1, getSwarmTier() / 2 + 1);
        }
        MobEntity innerEntity = getInnerEntity();
        if (innerEntity != null) {
            return innerEntity.getSize(poseIn);
        }
        return super.getSize(poseIn);
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
                Entity entity = entityType.create(world);
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
	 * @param world The world.
	 * @param blockPos The position.
     * @return If we are allowed to spawn a spirit.
     */
	@SuppressWarnings("unchecked")
	public static boolean canSpawnNew(World world, BlockPos blockPos) {
		int area = EntityVengeanceSpiritConfig.spawnLimitArea;
		int threshold = EntityVengeanceSpiritConfig.spawnLimit;
		AxisAlignedBB box = new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX(), blockPos.getY(), blockPos.getZ()).grow(area, area, area);
    	List<EntityVengeanceSpirit> spirits = world.getEntitiesWithinAABB(EntityVengeanceSpirit.class, box);
		if(spirits.size() >= threshold) {
            return false;
        }

        return WorldHelpers.foldArea(world, BlockGemStoneTorchConfig.area, blockPos,
                (input, world1, blockPos1) -> input
                        && !BlockTags.getCollection().get(new ResourceLocation("evilcraft:vengeance_spirit_blocker")).contains(world1.getBlockState(blockPos1).getBlock()), true);
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
		if(world.isRemote()) {
			showBurstParticles(hitX, hitY, hitZ, impactMotionX, impactMotionY, impactMotionZ);
		}
	}

	@OnlyIn(Dist.CLIENT)
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

            Minecraft.getInstance().worldRenderer.addParticle(
                    new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                    hitX, hitY, hitZ, dx, dy, dz);
		}
	}

	/**
	 * Spawn a random vengeance spirit in the given area.
	 * @param world The world.
	 * @param blockPos The position.
	 * @param area The radius in which the spawn can occur.
	 * @return The spawned spirit, could be null.
	 */
	public static EntityVengeanceSpirit spawnRandom(World world, BlockPos blockPos, int area) {
		EntityVengeanceSpirit spirit = new EntityVengeanceSpirit(world);
		int attempts = 50;
        int baseDistance = 5;
		while(canSpawnNew(world, blockPos) && attempts > 0) {
            BlockPos spawnPos = blockPos.add(
                    MathHelper.nextInt(world.rand, baseDistance, baseDistance + area) * MathHelper.nextInt(world.rand, -1, 1),
                    MathHelper.nextInt(world.rand, 0, 3) * MathHelper.nextInt(world.rand, -1, 1),
                    MathHelper.nextInt(world.rand, baseDistance, baseDistance + area) * MathHelper.nextInt(world.rand, -1, 1)
            );
            
            if(BlockHelpers.doesBlockHaveSolidTopSurface(world, spawnPos.add(0, -1, 0))) {
                spirit.setPosition((double) spawnPos.getX() + 0.5, (double) spawnPos.getY() + 0.5, (double) spawnPos.getZ() + 0.5);
                if(!world.hasNoCollisions(spirit)
                		&& spirit.isNotColliding(world)
                		&& !world.containsAnyLiquid(spirit.getBoundingBox())) {
                    world.addEntity(spirit);
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
    public boolean handleFluidAcceleration(ITag<Fluid> fluidTag, double p_210500_2_) {
        // Ignore water movement and particles
        return this.inWater;
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    public static EntityVengeanceSpirit fromNBT(World world, CompoundNBT spiritTag) {
        EntityVengeanceSpirit spirit = new EntityVengeanceSpirit(world);
        spirit.readAdditional(spiritTag);
        return spirit;
    }

    public void addEntanglingPlayer(ServerPlayerEntity player) {
        this.entanglingPlayers.add(player);
    }

    public Set<ServerPlayerEntity> getEntanglingPlayers() {
        return entanglingPlayers;
    }
}
