package org.cyclops.evilcraft.entity.monster;

import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * A zombie that is controlled by the player that spawned them with the {@link evilcraft.item.NecromancerStaff}.
 * @author rubensworks
 *
 */
public class ControlledZombie extends EntityMob implements IConfigurable {

    private static final int WATCHERID_TTL = 20;

    /**
     * Make a new instance.
     * @param world The world.
     */
    public ControlledZombie(World world) {
        super(world);
        addPotionEffect(new PotionEffect(Potion.confusion.id, 2000, 0));
        this.getNavigator().setAvoidsWater(false);
        this.getNavigator().setBreakDoors(true);

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityLiving.class, 1.0D, true));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 64.0F));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAILookIdle(this));

        this.setSize(0.6F, 1.8F);
    }

    @Override
    protected boolean isAIEnabled() {
        return true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataWatcher().addObject(12, Byte.valueOf((byte) 0));
        this.getDataWatcher().addObject(13, Byte.valueOf((byte) 0));
        this.getDataWatcher().addObject(14, Byte.valueOf((byte) 0));
        this.dataWatcher.addObject(WATCHERID_TTL, 0);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("ttl", getTtl());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        setTtl(tag.getInteger("ttl"));
    }

    public int getTtl() {
        return dataWatcher.getWatchableObjectInt(WATCHERID_TTL);
    }

    public void setTtl(int ttl) {
        this.dataWatcher.updateObject(WATCHERID_TTL, ttl);
    }

    @Override
    public boolean canAttackClass(Class clazz) {
        return true;
    }

    @Override
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {}

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }

    @Override
    protected String getLivingSound() {
        return "mob.zombie.say";
    }

    @Override
    protected String getHurtSound() {
        return "mob.zombie.hurt";
    }

    @Override
    protected String getDeathSound() {
        return "mob.zombie.death";
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(!worldObj.isRemote) {
            int ttl = getTtl();
            setTtl(--ttl);
            if (ttl == 0) {
                setDead();
            }
        }
    }
}