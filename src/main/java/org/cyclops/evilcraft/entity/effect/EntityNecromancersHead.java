package org.cyclops.evilcraft.entity.effect;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.Advancements;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityControlledZombie;
import org.cyclops.evilcraft.item.ItemNecromancerStaff;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

/**
 * Entity for the {@link ItemNecromancerStaff}.
 * @author rubensworks
 *
 */
@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
public class EntityNecromancersHead extends ThrowableEntity implements IRendersAsItem {
    
	private static final int DURATION = 200;
	private static final ItemStack RENDER_ITEM = new ItemStack(Items.SKELETON_SKULL);

	protected boolean observing = false;
	protected LivingEntity target = null;
	protected List<EntityControlledZombie> observables = Lists.newLinkedList();
	protected Class<? extends MobEntity> mobType = EntityControlledZombie.class;

	public EntityNecromancersHead(EntityType<? extends EntityNecromancersHead> type, World world) {
		super(type, world);
	}

	public EntityNecromancersHead(World world, LivingEntity entity) {
		super(RegistryEntries.ENTITY_NECROMANCER_HEAD, entity, world);
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
    
    /**
     * Set the type of mob to be spawned. Should only be called server-side.
     * @param mobType The mob type.
     */
    public void setMobType(Class<? extends MobEntity> mobType) {
    	this.mobType = mobType;
    }
    
    protected void spawnSwarm(LivingEntity necromancer, LivingEntity target) {
    	World world = target.world;
    	int amount = world.rand.nextInt(2) + 3;
    	for(int i = 0; i < amount; i++) {
			EntityControlledZombie mob = new EntityControlledZombie(world);
			if(mob.canAttack(target.getType())) {
				mob.copyLocationAndAnglesFrom(necromancer);
				mob.move(MoverType.SELF, new Vec3d(world.rand.nextInt(20) - 10, 0, world.rand.nextInt(20) - 10));
				if(EntityHelpers.spawnEntity(world, mob, SpawnReason.MOB_SUMMONED)) {
					observables.add(mob);
				}
				mob.setAttackTarget(target);
				mob.setTtl(DURATION);
			}
    	}
    	this.target = target;
    	setObserverMode();

		if (necromancer instanceof ServerPlayerEntity) {
			Advancements.NECROMANCE.trigger((ServerPlayerEntity) necromancer, target);
		}
    }
    
    @Override
    public void tick() {
    	super.tick();
    	if(observing && !world.isRemote() && WorldHelpers.efficientTick(world, 10)) {
    		if(!observables.isEmpty()) {
    			Iterator<EntityControlledZombie> it = observables.iterator();
    			while(it.hasNext()) {
    				EntityControlledZombie mob = it.next();
    				if(!mob.isAlive() || !target.isAlive()) {
    					if(mob.isAlive()) {
                            mob.remove();
						}
    					it.remove();
    				}
				}
    		}
    		if(observables.isEmpty()) {
    			observing = false;
				this.remove();
			}
    	}
    }

	@Override
	protected void registerData() {

	}

	@Override
    public void remove() {
    	if(!observing) {
    		super.remove();
    	}
    }
    
    protected void setObserverMode() {
    	observing = true;
    	setMotion(0, 0, 0);
    	setInvisible(true);
    }

    @Override
    protected void onImpact(RayTraceResult position) {
    	if(position.getType() == RayTraceResult.Type.ENTITY && !observing && !getEntityWorld().isRemote()) {
			((EntityRayTraceResult) position).getEntity().attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
	        if(getThrower() instanceof ServerPlayerEntity
					&& getThrower() != ((EntityRayTraceResult) position).getEntity()
					&& ((EntityRayTraceResult) position).getEntity() instanceof LivingEntity) {
	        	spawnSwarm(this.getThrower(), (LivingEntity) ((EntityRayTraceResult) position).getEntity());
	        } else {
				this.remove();
	        }
    	}
    }

	@Override
	public ItemStack getItem() {
		return RENDER_ITEM;
	}
}
