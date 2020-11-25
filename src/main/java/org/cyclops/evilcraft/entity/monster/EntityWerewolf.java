package org.cyclops.evilcraft.entity.monster;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Random;

/**
 * A large werewolf, only appears at night by transforming from a werewolf villager.
 * @author rubensworks
 *
 */
public class EntityWerewolf extends MonsterEntity {

    static {
        MinecraftForge.EVENT_BUS.register(EntityWerewolf.class);
    }
    
    private CompoundNBT villagerNBTTagCompound = new CompoundNBT();
    private boolean fromVillager = false;
    
    private static final int BARKCHANCE = 1000;
    private static final int BARKLENGTH = 40;
    private static int barkprogress = -1;

    public EntityWerewolf(EntityType<? extends EntityWerewolf> type,  World world) {
        super(type, world);
    }

    public EntityWerewolf(World world) {
        super(RegistryEntries.ENTITY_WEREWOLF, world);

        this.stepHeight = 1.0F;
        
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(2, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, PlayerEntity.class, true));
    
        // This sets the default villager profession ID.
        this.villagerNBTTagCompound.putString("ProfessionName", RegistryEntries.VILLAGER_PROFESSION_WEREWOLF.getRegistryName().toString());
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void transformWerewolfVillager(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntity() instanceof VillagerEntity && !event.getEntity().world.isRemote()) {
            VillagerEntity villager = (VillagerEntity) event.getEntity();
            if(EntityWerewolf.isWerewolfTime(event.getEntity().world)
                    && villager.getVillagerData().getProfession() == RegistryEntries.VILLAGER_PROFESSION_WEREWOLF
                    && villager.world.getLightFor(LightType.SKY, villager.getPosition()) > 0) {
                EntityWerewolf.replaceVillager(villager);
            }
        }
    }
    
    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.75F;
    }
    
    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
    }
    
    @Override
    public void writeAdditional(CompoundNBT NBTTagCompound) {
        super.writeAdditional(NBTTagCompound);
        NBTTagCompound.put("villager", villagerNBTTagCompound);
        NBTTagCompound.putBoolean("fromVillager", fromVillager);
    }

    @Override
    public void readAdditional(CompoundNBT NBTTagCompound) {
        super.readAdditional(NBTTagCompound);
        this.villagerNBTTagCompound = NBTTagCompound.getCompound("villager");
        this.fromVillager = NBTTagCompound.getBoolean("fromVillager");
    }
    
    /**
     * If at the current time in the given world werewolves can appear.
     * @param world The world.
     * @return If it is werewolf party time.
     */
    public static boolean isWerewolfTime(World world) {
        return world.getCurrentMoonPhaseFactor() == 1.0
                && !world.isDaytime()
                && world.getDifficulty() != Difficulty.PEACEFUL;
    }
    
    private static void replaceEntity(MobEntity old, MobEntity neww, World world) {
        // TODO: A nice update effect?
        // Maybe something like this: https://github.com/iChun/Morph/blob/master/morph/client/model/ModelMorphAcquisition.java
        neww.copyLocationAndAnglesFrom(old);
        old.remove();

        world.addEntity(neww);
        world.playEvent(null, 1016, old.getPosition(), 0);
    }
    
    /**
     * Replace this entity with the stored villager.
     */
    public void replaceWithVillager() {
        VillagerEntity villager = new VillagerEntity(EntityType.VILLAGER, this.world, IVillagerType.byBiome(this.world.getBiome(getPosition())));
        villager.setVillagerData(villager.getVillagerData().withProfession(RegistryEntries.VILLAGER_PROFESSION_WEREWOLF));
        replaceEntity(this, villager, this.world);
        try {
            villager.readAdditional(villagerNBTTagCompound);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Replace the given villager with a werewolf and store the data of that villager.
     * @param villager The villager to replace.
     */
    public static void replaceVillager(VillagerEntity villager) {
        EntityWerewolf werewolf = new EntityWerewolf(villager.world);
        villager.writeAdditional(werewolf.getVillagerNBTTagCompound());
        werewolf.setFromVillager(true);
        replaceEntity(villager, werewolf, villager.world);
    }
    
    @Override
    public void livingTick() {
        if(!world.isRemote() && (!isWerewolfTime(world) || world.getDifficulty() == Difficulty.PEACEFUL)) {
            replaceWithVillager();
        } else {
            super.livingTick();
        }
        
        // Random barking
        Random random = world.rand;
        if(random.nextInt(BARKCHANCE) == 0 && barkprogress == -1) {
            barkprogress++;
        } else if(barkprogress > -1) {
            playSound(SoundEvents.ENTITY_WOLF_GROWL, 0.15F, 1.0F);
            barkprogress++;
            if(barkprogress > BARKLENGTH) {
                barkprogress = -1;
            }
        }
    }
    
    /**
     * Get the bark progress scaled to the given parameter.
     * @param scale The scale.
     * @return The scaled progress.
     */
    public float getBarkProgressScaled(float scale) {
        if(barkprogress == -1)
            return 0;
        else
            return (float)barkprogress / (float)BARKLENGTH * scale;
    }

    @Override
    public ResourceLocation getLootTable() {
        return new ResourceLocation(Reference.MOD_ID, "entities/werewolf");
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WOLF_GROWL;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }

    @Override
    public SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState block) {
        this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
    }
    
    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return super.canDespawn(distanceToClosestPlayer) && !isFromVillager();
    }
    
    /**
     * Get the villager data.
     * @return Villager data.
     */
    public CompoundNBT getVillagerNBTTagCompound() {
        return villagerNBTTagCompound;
    }
    
    /**
     * If this werewolf was created from a transforming villager.
     * @return If it was a villager.
     */
    public boolean isFromVillager() {
        return fromVillager;
    }
    
    /**
     * Set is from villager.
     * @param fromVillager If this werewolf is a transformed villager.
     */
    public void setFromVillager(boolean fromVillager) {
        this.fromVillager = fromVillager;
    }

}