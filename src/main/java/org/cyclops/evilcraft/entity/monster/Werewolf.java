package org.cyclops.evilcraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.entity.villager.WerewolfVillagerConfig;

import java.util.Random;

/**
 * A large werewolf, only appears at night by transforming from a werewolf villager.
 * @author rubensworks
 *
 */
public class Werewolf extends EntityMob implements IConfigurable{
    
    private NBTTagCompound villagerNBTTagCompound = new NBTTagCompound();
    private boolean fromVillager = false;
    
    private static final int BARKCHANCE = 1000;
    private static final int BARKLENGTH = 40;
    private static int barkprogress = -1;

    /**
     * Make a new instance.
     * @param world The world.
     */
    public Werewolf(World world) {
        super(world);

        this.setSize(0.6F, 2.9F);
        this.stepHeight = 1.0F;
        this.isImmuneToFire = false;
        
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWander(this, 0.6D));
        this.tasks.addTask(2, new EntityAILookIdle(this));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false, false));
    
        // This sets the default villager profession ID.
        if(Configs.isEnabled(WerewolfVillagerConfig.class)) {
            this.villagerNBTTagCompound.setInteger("Profession", WerewolfVillagerConfig._instance.getId());
        }
    }
    
    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.75F;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.625D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
    }
    
    @Override
    public void writeEntityToNBT(NBTTagCompound NBTTagCompound) {
        super.writeEntityToNBT(NBTTagCompound);
        NBTTagCompound.setTag("villager", villagerNBTTagCompound);
        NBTTagCompound.setBoolean("fromVillager", fromVillager);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound NBTTagCompound) {
        super.readEntityFromNBT(NBTTagCompound);
        this.villagerNBTTagCompound = NBTTagCompound.getCompoundTag("villager");
        this.fromVillager = NBTTagCompound.getBoolean("fromVillager");
    }
    
    /**
     * If at the current time in the given world werewolves can appear.
     * @param world The world.
     * @return If it is werewolf party time.
     */
    public static boolean isWerewolfTime(World world) {
        return world.getCurrentMoonPhaseFactor() == 1.0
                && !MinecraftHelpers.isDay(world)
                && world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }
    
    private static void replaceEntity(EntityLiving old, EntityLiving neww, World world) {
        // TODO: A nice update effect?
        // Maybe something like this: https://github.com/iChun/Morph/blob/master/morph/client/model/ModelMorphAcquisition.java
        neww.copyLocationAndAnglesFrom(old);
        world.removeEntity(old);

        world.spawnEntityInWorld(neww);
        world.playAuxSFXAtEntity(null, 1016, old.getPosition(), 0);
    }
    
    /**
     * Replace this entity with the stored villager.
     */
    public void replaceWithVillager() {
        if(Configs.isEnabled(WerewolfVillagerConfig.class) && false) { // TODO: re-enable when reimplementing werewolf villagers
            EntityVillager villager = new EntityVillager(this.worldObj, WerewolfVillagerConfig._instance.getId());
            replaceEntity(this, villager, this.worldObj);
            villager.readEntityFromNBT(villagerNBTTagCompound);
        }
    }
    
    /**
     * Replace the given villager with a werewolf and store the data of that villager.
     * @param villager The villager to replace.
     */
    public static void replaceVillager(EntityVillager villager) {
        if(Configs.isEnabled(WerewolfConfig.class)) {
            Werewolf werewolf = new Werewolf(villager.worldObj);
            villager.writeEntityToNBT(werewolf.getVillagerNBTTagCompound());
            werewolf.setFromVillager(true);
            replaceEntity(villager, werewolf, villager.worldObj);
        }
    }
    
    @Override
    public void onLivingUpdate() {        
        if(!worldObj.isRemote && (!isWerewolfTime(worldObj) || worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)) {
            replaceWithVillager();
        } else {
            super.onLivingUpdate();
        }
        
        // Random barking
        Random random = worldObj.rand;
        if(random.nextInt(BARKCHANCE) == 0 && barkprogress == -1) {
            barkprogress++;
        } else if(barkprogress > -1) {
            playSound(SoundEvents.entity_wolf_growl, 0.15F, 1.0F);
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
    protected ResourceLocation getLootTable() {
        return new ResourceLocation(Reference.MOD_ID, "entities/werewolf");
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.entity_wolf_growl;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return SoundEvents.entity_wolf_hurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.entity_wolf_death;
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        this.playSound(SoundEvents.entity_zombie_step, 0.15F, 1.0F);
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }
    
    @Override
    protected boolean canDespawn() {
        return !isFromVillager();
    }
    
    /**
     * Get the villager data.
     * @return Villager data.
     */
    public NBTTagCompound getVillagerNBTTagCompound() {
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

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }

}