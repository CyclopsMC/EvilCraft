package org.cyclops.evilcraft.entity.effect;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.Achievements;
import org.cyclops.evilcraft.entity.monster.ControlledZombie;
import org.cyclops.evilcraft.item.NecromancerStaff;

import java.util.Iterator;
import java.util.List;

/**
 * Entity for the {@link NecromancerStaff}.
 * @author rubensworks
 *
 */
public class EntityNecromancersHead extends EntityThrowable implements IConfigurable {
    
	private static final int DURATION = 200;

	protected boolean observing = false;
	protected EntityLivingBase target = null;
	protected List<ControlledZombie> observables = Lists.newLinkedList();
	protected Class<? extends EntityLiving> mobType = ControlledZombie.class;
	
    /**
     * Make a new instance in the given world.
     * @param world The world to make it in.
     */
    public EntityNecromancersHead(World world) {
        super(world);
    }

    /**
     * Make a new instance in a world by a placer {@link EntityLivingBase}.
     * @param world The world.
     * @param entity The {@link EntityLivingBase} that placed this {@link Entity}.
     */
    public EntityNecromancersHead(World world, EntityLivingBase entity) {
        super(world, entity);
    }
    
    /**
     * Make a new instance at the given location in a world.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    @SideOnly(Side.CLIENT)
    public EntityNecromancersHead(World world, double x, double y, double z) {
        super(world, x, y, z);
    }
    
    /**
     * Set the type of mob to be spawned. Should only be called server-side.
     * @param mobType The mob type.
     */
    public void setMobType(Class<? extends EntityLiving> mobType) {
    	this.mobType = mobType;
    }
    
    protected void spawnSwarm(EntityLivingBase necromancer, EntityLivingBase target) {
    	World world = target.worldObj;
    	int amount = world.rand.nextInt(2) + 3;
    	for(int i = 0; i < amount; i++) {
			ControlledZombie mob = new ControlledZombie(world);
			if(mob.canAttackClass(target.getClass())) {
				mob.copyLocationAndAnglesFrom(necromancer);
				mob.moveEntity(world.rand.nextInt(20) - 10, 0, world.rand.nextInt(20) - 10);
				if(EntityHelpers.spawnEntity(world, mob)) {
					observables.add(mob);
				}
				mob.setAttackTarget(target);
				mob.setTtl(DURATION);
			}
    	}
    	this.target = target;
    	setObserverMode();
    	if(necromancer instanceof EntityPlayer && target instanceof EntityPlayer) {
    		((EntityPlayer) necromancer).addStat(Achievements.PLAYER_DEVASTATOR, 1);
    	}
    }
    
    @Override
    public void onUpdate() {
    	super.onUpdate();
    	if(observing && !worldObj.isRemote && WorldHelpers.efficientTick(worldObj, 10)) {
    		if(!observables.isEmpty()) {
    			Iterator<ControlledZombie> it = observables.iterator();
    			while(it.hasNext()) {
    				ControlledZombie mob = it.next();
    				if(!mob.isEntityAlive() || !target.isEntityAlive()) {
    					if(mob.isEntityAlive()) {
                            mob.setDead();
						}
    					it.remove();
    				}
				}
    		}
    		if(observables.isEmpty()) {
    			observing = false;
				this.setDead();
			}
    	}
    }
    
    @Override
    public void setDead() {
    	if(!observing) {
    		super.setDead();
    	}
    }
    
    protected void setObserverMode() {
    	observing = true;
    	motionX = 0;
    	motionY = 0;
    	motionZ = 0;
    	setInvisible(true);
    }

    @Override
    protected void onImpact(RayTraceResult position) {
    	if(!observing) {
	        if(position.entityHit != null) {
	            position.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
	        }
	        if(this.getThrower() != null && !getThrower().worldObj.isRemote
	        		&& getThrower() instanceof EntityPlayerMP && position.entityHit instanceof EntityLivingBase) {
	        	spawnSwarm(this.getThrower(), (EntityLivingBase) position.entityHit);
	        } else {
	        	this.setDead();
	        }
    	}
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }

}
