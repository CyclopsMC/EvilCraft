package org.cyclops.evilcraft.entity.monster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemNecromancerStaff;

/**
 * A zombie that is controlled by the player that spawned them with the {@link ItemNecromancerStaff}.
 * @author rubensworks
 *
 */
public class EntityControlledZombie extends Monster {

    private static final EntityDataAccessor<Integer> WATCHERID_TTL = SynchedEntityData.<Integer>defineId(EntityControlledZombie.class, EntityDataSerializers.INT);

    public EntityControlledZombie(EntityType<? extends EntityControlledZombie> entityType, Level world) {
        super(entityType, world);
        addEffect(new MobEffectInstance(MobEffects.CONFUSION, 2000, 0));
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
    }

    public EntityControlledZombie(Level world) {
        this(RegistryEntries.ENTITY_CONTROLLED_ZOMBIE, world);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 64.0F));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglin.class));
        this.targetSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WATCHERID_TTL, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ttl", getTtl());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setTtl(tag.getInt("ttl"));
    }

    public int getTtl() {
        return entityData.get(WATCHERID_TTL);
    }

    public void setTtl(int ttl) {
        this.entityData.set(WATCHERID_TTL, ttl);
    }

    @Override
    public boolean canAttackType(EntityType<?> type) {
        return true;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(!level().isClientSide()) {
            int ttl = getTtl();
            setTtl(--ttl);
            if (ttl == 0) {
                remove(RemovalReason.DISCARDED);
            }
        }
    }
}
