package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Entity for the anti-vengeance beams.
 * @author rubensworks
 *
 */
public class EntityAntiVengeanceBeam extends ThrowableEntity {
	
	private static final int MAX_AGE = 10 * 20;
    
    private int age = 0;
    private int soundTick = 0;

    public EntityAntiVengeanceBeam(EntityType<? extends EntityAntiVengeanceBeam> type, World world) {
        super(type, world);
        setDeltaMovement(getDeltaMovement().multiply(0.25, 0.25, 0.25));
    }

    public EntityAntiVengeanceBeam(EntityType<? extends EntityAntiVengeanceBeam> type, World world, LivingEntity entity) {
        super(type, entity, world);
        setDeltaMovement(getDeltaMovement().multiply(0.25, 0.25, 0.25));
    }

    public EntityAntiVengeanceBeam(World world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_ANTI_VENGEANCE_BEAM, entity, world);
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    @Override
    protected float getGravity() {
        return 0.0F;
    }

    @Nullable
    protected EntityRayTraceResult rayTraceEntities(Vector3d startVec, Vector3d endVec) {
        return ProjectileHelper.getEntityHitResult(this.level, this, startVec, endVec, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D),
                (entity) -> !entity.isSpectator() && entity.isAlive() && entity.isPickable() && (entity != this.getOwner()));
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        Vector3d motion = getDeltaMovement();
    	Vector3d vec3 = new Vector3d(this.getX(), this.getY(), this.getZ());
        Vector3d vec31 = new Vector3d(this.getX(), this.getY(), this.getZ()).add(motion);
        EntityRayTraceResult entityRayTraceResult = this.rayTraceEntities(vec3, vec31);
        vec3 = new Vector3d(this.getX(), this.getY(), this.getZ());
        vec31 = new Vector3d(this.getX(), this.getY(), this.getZ()).add(motion);
        
        soundTick++;
        if(soundTick > 3 && this.getId() % 10 == 0) {
        	soundTick = 0;
        }
        
    	if (!this.level.isClientSide()) {
            Entity entity = null;
            List<Entity> list = this.level.getEntities(this, this.getBoundingBox()
                    .move(motion).inflate(1.0D));
            double d0 = 0.0D;

            for (Entity entity1 : list) {
                if (entity1 instanceof EntityVengeanceSpirit && !((EntityVengeanceSpirit) entity1).isSwarm()) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.getBoundingBox().inflate((double) f);
                    EntityRayTraceResult movingobjectposition1 = ProjectileHelper.getEntityHitResult(level, this, vec3, vec31, axisalignedbb, (e) -> true);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3.distanceTo(movingobjectposition1.getLocation());

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                entityRayTraceResult = new EntityRayTraceResult(entity);
            }
        } else {
        	for(int i = 0; i < level.random.nextInt(5) + 5; i++) {
        		showNewBlurParticle();
        	}
        	if(soundTick == 1) {
	        	// Play beam sound
	        	level.playLocalSound(getX(), getY(), getZ(),
                        EvilCraftSoundEvents.effect_vengeancebeam_base, SoundCategory.NEUTRAL,
                        0.5F + level.random.nextFloat() * 0.2F, 1.0F, false);
        	}
        }
    	
    	if (entityRayTraceResult != null) {
    	    this.onHit(entityRayTraceResult);
        }
    	
    	if(age++ > MAX_AGE) {
    		this.remove();
    	}
    	
    	super.tick();
    }

    @OnlyIn(Dist.CLIENT)
    protected void showNewBlurParticle() {
    	float scale = 0.6F - random.nextFloat() * 0.3F;
    	float red = random.nextFloat() * 0.03F + 0.01F;
        float green = random.nextFloat() * 0.03F;
        float blue = random.nextFloat() * 0.05F + 0.05F;
        float ageMultiplier = (float) (random.nextDouble() * 6.5D + 4D);
        Vector3d motion = getDeltaMovement();

        Minecraft.getInstance().levelRenderer.addParticle(
                new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                getX(), getY(), getZ(),
                deriveMotion(motion.x), deriveMotion(motion.y), deriveMotion(motion.z));
	}
    
    private double deriveMotion(double motion) {
    	return motion * 0.5D + (0.02D - random.nextDouble() * 0.04D);
    }

    protected void applyHitEffect(Entity entity) {
        if (entity instanceof EntityVengeanceSpirit) {
            Vector3d motion = getDeltaMovement();
            ((EntityVengeanceSpirit) entity).onHit(getX(), getY(), getZ(), motion.x, motion.y, motion.z);
            if (getOwner() instanceof ServerPlayerEntity) {
                ((EntityVengeanceSpirit) entity).addEntanglingPlayer((ServerPlayerEntity) getOwner());
            }
        }
    }

    @Override
    protected void onHit(RayTraceResult position) {
        if (!this.level.isClientSide()) {
            if (position.getType() == RayTraceResult.Type.ENTITY && this.getOwner() != null && this.getOwner() instanceof ServerPlayerEntity) {
                applyHitEffect(((EntityRayTraceResult) position).getEntity());
            }
        }
        this.remove();
    }
}
