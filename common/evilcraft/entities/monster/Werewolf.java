package evilcraft.entities.monster;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import evilcraft.api.Helpers;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.entities.villager.WerewolfVillagerConfig;
import evilcraft.items.WerewolfBoneConfig;

public class Werewolf extends EntityMob implements Configurable{
    
    protected ExtendedConfig eConfig = null;
    
    public static ElementType TYPE = ElementType.MOB;
    
    private NBTTagCompound villagerNBTTagCompound = new NBTTagCompound();
    private boolean fromVillager = false;

    // Set a configuration for this entity
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }

    public Werewolf(World world) {
        super(world);
        
        this.getNavigator().setAvoidsWater(true);
        this.setSize(0.6F, 2.9F);
        this.stepHeight = 1.0F;
        this.isImmuneToFire = false;
        
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWander(this, 1.0F));
        this.tasks.addTask(2, new EntityAILookIdle(this));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false));
    
        // This sets the default villager profession ID.
        this.villagerNBTTagCompound.setInteger("Profession", WerewolfVillagerConfig._instance.ID);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(7.0D);
    }
    
    public void writeEntityToNBT(NBTTagCompound NBTTagCompound) {
        super.writeEntityToNBT(NBTTagCompound);
        NBTTagCompound.setCompoundTag("villager", villagerNBTTagCompound);
        NBTTagCompound.setBoolean("fromVillager", fromVillager);
    }

    public void readEntityFromNBT(NBTTagCompound NBTTagCompound) {
        super.readEntityFromNBT(NBTTagCompound);
        this.villagerNBTTagCompound = NBTTagCompound.getCompoundTag("villager");
        this.fromVillager = NBTTagCompound.getBoolean("fromVillager");
    }
    
    public static boolean isWerewolfTime(World world) {
        return world.getMoonPhase() == 0 && !Helpers.isDay(world);
    }
    
    private static void replaceEntity(EntityLiving old, EntityLiving neww, World world) {
        // TODO: A nice update effect?
        // Maybe something like this: https://github.com/iChun/Morph/blob/master/morph/client/model/ModelMorphAcquisition.java
        neww.copyLocationAndAnglesFrom(old);
        world.removeEntity(old);
        neww.onSpawnWithEgg((EntityLivingData)null);

        world.spawnEntityInWorld(neww);
        world.playAuxSFXAtEntity((EntityPlayer)null, 1016, (int)old.posX, (int)old.posY, (int)old.posZ, 0);
    }
    
    public void replaceWithVillager() {
        EntityVillager villager = new EntityVillager(this.worldObj, WerewolfVillagerConfig._instance.ID);
        replaceEntity(this, villager, this.worldObj);
        villager.readEntityFromNBT(villagerNBTTagCompound);
    }
    
    public static void replaceVillager(EntityVillager villager) {
        Werewolf werewolf = new Werewolf(villager.worldObj);
        villager.writeEntityToNBT(werewolf.getVillagerNBTTagCompound());
        werewolf.setFromVillager(true);
        replaceEntity(villager, werewolf, villager.worldObj);
    }
    
    @Override
    public void onLivingUpdate() {
        if(!worldObj.isRemote && !isWerewolfTime(worldObj)) {
            replaceWithVillager();
        } else {
            super.onLivingUpdate();
        }
    }
    
    @Override
    protected int getDropItemId() {
        if(WerewolfBoneConfig._instance.isEnabled())
            return WerewolfBoneConfig._instance.ID;
        else
            return super.getDropItemId();
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.wolf.growl";
    }

    @Override
    protected String getHurtSound() {
        return "mob.wolf.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.wolf.death";
    }
    
    @Override
    protected void playStepSound(int par1, int par2, int par3, int par4) {
        this.playSound("mob.zombie.step", 0.15F, 1.0F);
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }
    
    @Override
    protected boolean canDespawn() {
        return !isFromVillager();
    }
    
    public NBTTagCompound getVillagerNBTTagCompound() {
        return villagerNBTTagCompound;
    }
    
    public boolean isFromVillager() {
        return fromVillager;
    }
    
    public void setFromVillager(boolean fromVillager) {
        this.fromVillager = fromVillager;
    }

    @Override
    public String getUniqueName() {
        return "entities.monster."+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return true;
    }

}
