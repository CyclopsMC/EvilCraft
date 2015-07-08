package evilcraft.entity.effect;

import com.google.common.collect.Lists;
import evilcraft.Achievements;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import evilcraft.item.NecromancerStaff;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

/**
 * Entity for the {@link NecromancerStaff}.
 * @author rubensworks
 *
 */
public class EntityNecromancersHead extends EntityThrowable implements IConfigurable {
    
	private static final int DURATION = 20;

	protected boolean observing = false;
	protected EntityLivingBase target = null;
	protected List<EntityLiving> observables = Lists.newLinkedList();
	protected int timeLeft = DURATION;
	protected Class<? extends EntityLiving> mobType = EntityZombie.class;
	
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
			try {
				Constructor<? extends EntityLiving> constructor = mobType.getConstructor(World.class);
				EntityLiving mob = constructor.newInstance(world);
	    		if(mob.canAttackClass(target.getClass())) {
			    	mob.copyLocationAndAnglesFrom(necromancer);
			    	mob.moveEntity(world.rand.nextInt(20) - 10, 0, world.rand.nextInt(20) - 10);
			    	mob.setHealth(mob.getHealth() / 2);
			    	mob.setRevengeTarget(target);
			    	mob.addPotionEffect(new PotionEffect(Potion.confusion.id, 2000, 0));
			    	mob.captureDrops = true; // Don't drop loot on death
	            	if(EntityHelpers.spawnEntity(world, mob)) {
                        observables.add(mob);
			    	}
                    boolean res = mob.getNavigator().tryMoveToEntityLiving(target, 1);
                    if(!res) {
                        mob.getNavigator().tryMoveToXYZ(target.posX, target.posY + 1, target.posZ, 1);
                    }
	    		}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				setDead();
			} catch (SecurityException e) {
				e.printStackTrace();
				setDead();
			} catch (InstantiationException e) {
				e.printStackTrace();
				setDead();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				setDead();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				setDead();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				setDead();
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
    		timeLeft--;
    		if(!observables.isEmpty()) {
    			Iterator<EntityLiving> it = observables.iterator();
    			while(it.hasNext()) {
    				EntityLiving mob = it.next();
    				if(!mob.isEntityAlive() || !target.isEntityAlive() || timeLeft <= 0) {
    					if(mob.isEntityAlive()) {
                            mob.setDead();
    					}
    					it.remove();
    				} else {
                        mob.setLastAttacker(target);
                        mob.setRevengeTarget(target);
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
    protected void onImpact(MovingObjectPosition position) {
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
