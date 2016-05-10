package org.cyclops.evilcraft.entity.monster;

import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

/**
 * A zombie that is controlled by the player that spawned them with the {@link org.cyclops.evilcraft.item.NecromancerStaff}.
 * @author rubensworks
 *
 */
public class ControlledZombie extends EntityMob implements IConfigurable {

    private static final DataParameter<Integer> WATCHERID_TTL = EntityDataManager.<Integer>createKey(ControlledZombie.class, DataSerializers.VARINT);

    /**
     * Make a new instance.
     * @param world The world.
     */
    public ControlledZombie(World world) {
        super(world);
        addPotionEffect(new PotionEffect(MobEffects.confusion, 2000, 0));
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);

        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 64.0F));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAILookIdle(this));

        this.setSize(0.6F, 1.8F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.register(WATCHERID_TTL, 0);
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
        return dataWatcher.get(WATCHERID_TTL);
    }

    public void setTtl(int ttl) {
        this.dataWatcher.set(WATCHERID_TTL, ttl);
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
    protected SoundEvent getAmbientSound() {
        return SoundEvents.entity_zombie_ambient;
    }

    @Override
    protected SoundEvent getHurtSound() {
        return SoundEvents.entity_zombie_hurt;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.entity_zombie_death;
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