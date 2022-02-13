package org.cyclops.evilcraft.entity.monster;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.item.ItemNecromancerStaff;

/**
 * A zombie that is controlled by the player that spawned them with the {@link ItemNecromancerStaff}.
 * @author rubensworks
 *
 */
public class EntityControlledZombie extends MonsterEntity {

    private static final DataParameter<Integer> WATCHERID_TTL = EntityDataManager.<Integer>defineId(EntityControlledZombie.class, DataSerializers.INT);

    public EntityControlledZombie(EntityType<? extends EntityControlledZombie> entityType, World world) {
        super(entityType, world);
        addEffect(new EffectInstance(Effects.CONFUSION, 2000, 0));
        ((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(true);
    }

    public EntityControlledZombie(World world) {
        this(RegistryEntries.ENTITY_CONTROLLED_ZOMBIE, world);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 64.0F));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglinEntity.class));
        this.targetSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WATCHERID_TTL, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("ttl", getTtl());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
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
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(!level.isClientSide()) {
            int ttl = getTtl();
            setTtl(--ttl);
            if (ttl == 0) {
                remove();
            }
        }
    }
}