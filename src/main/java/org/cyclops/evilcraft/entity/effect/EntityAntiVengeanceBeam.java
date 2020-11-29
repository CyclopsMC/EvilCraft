package org.cyclops.evilcraft.entity.effect;

import net.minecraft.block.NetherPortalBlock;
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
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.cyclops.cyclopscore.client.particle.ParticleBlurData;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit;

import javax.annotation.Nonnull;
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
        setMotion(getMotion().mul(0.25, 0.25, 0.25));
    }

    public EntityAntiVengeanceBeam(EntityType<? extends EntityAntiVengeanceBeam> type, World world, LivingEntity entity) {
        super(type, entity, world);
        setMotion(getMotion().mul(0.25, 0.25, 0.25));
    }

    public EntityAntiVengeanceBeam(World world, LivingEntity entity) {
        super(RegistryEntries.ENTITY_ANTI_VENGEANCE_BEAM, entity, world);
    }

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    
    @Override
    protected float getGravityVelocity() {
        return 0.0F;
    }

	@Override
    public void tick() {
        Vec3d motion = getMotion();
    	Vec3d vec3 = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
        Vec3d vec31 = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ()).add(motion);
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(new RayTraceContext(vec3, vec31, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        vec3 = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
        vec31 = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ()).add(motion);
        
        soundTick++;
        if(soundTick > 3 && this.getEntityId() % 10 == 0) {
        	soundTick = 0;
        }
        
    	if (!this.world.isRemote()) {
            Entity entity = null;
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox()
                    .offset(motion).grow(1.0D));
            double d0 = 0.0D;

            for (Entity entity1 : list) {
                if (entity1 instanceof EntityVengeanceSpirit && !((EntityVengeanceSpirit) entity1).isSwarm()) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow((double) f);
                    EntityRayTraceResult movingobjectposition1 = ProjectileHelper.rayTraceEntities(world, this, vec3, vec31, axisalignedbb, (e) -> true);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3.distanceTo(movingobjectposition1.getHitVec());

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new EntityRayTraceResult(entity);
            }
        } else {
        	for(int i = 0; i < world.rand.nextInt(5) + 5; i++) {
        		showNewBlurParticle();
        	}
        	if(soundTick == 1) {
	        	// Play beam sound
	        	world.playSound(getPosX(), getPosY(), getPosZ(),
                        EvilCraftSoundEvents.effect_vengeancebeam_base, SoundCategory.NEUTRAL,
                        0.5F + world.rand.nextFloat() * 0.2F, 1.0F, false);
        	}
        }
    	
    	if (movingobjectposition != null) {
            if (movingobjectposition.getType() == RayTraceResult.Type.BLOCK
                    && this.world.getBlockState(((BlockRayTraceResult) movingobjectposition).getPos()).getBlock() instanceof NetherPortalBlock) {
                this.inPortal = true;
            } else {
                this.onImpact(movingobjectposition);
            }
        }
    	
    	if(age++ > MAX_AGE) {
    		this.remove();
    	}
    	
    	super.tick();
    }

    @OnlyIn(Dist.CLIENT)
    protected void showNewBlurParticle() {
    	float scale = 0.6F - rand.nextFloat() * 0.3F;
    	float red = rand.nextFloat() * 0.03F + 0.01F;
        float green = rand.nextFloat() * 0.03F;
        float blue = rand.nextFloat() * 0.05F + 0.05F;
        float ageMultiplier = (float) (rand.nextDouble() * 6.5D + 4D);
        Vec3d motion = getMotion();

        Minecraft.getInstance().worldRenderer.addParticle(
                new ParticleBlurData(red, green, blue, scale, ageMultiplier), false,
                getPosX(), getPosY(), getPosZ(),
                deriveMotion(motion.x), deriveMotion(motion.y), deriveMotion(motion.z));
	}
    
    private double deriveMotion(double motion) {
    	return motion * 0.5D + (0.02D - rand.nextDouble() * 0.04D);
    }

    protected void applyHitEffect(Entity entity) {
        if (entity instanceof EntityVengeanceSpirit) {
            Vec3d motion = getMotion();
            ((EntityVengeanceSpirit) entity).onHit(getPosX(), getPosY(), getPosZ(), motion.x, motion.y, motion.z);
            if (getThrower() instanceof ServerPlayerEntity) {
                ((EntityVengeanceSpirit) entity).addEntanglingPlayer((ServerPlayerEntity) getThrower());
            }
        }
    }

	@Override
    protected void onImpact(RayTraceResult position) {
        if (!this.world.isRemote()) {
            if (position.getType() == RayTraceResult.Type.ENTITY && this.getThrower() != null && this.getThrower() instanceof ServerPlayerEntity) {
                applyHitEffect(((EntityRayTraceResult) position).getEntity());
            }
        }
        this.remove();
    }

    @Override
    protected void registerData() {

    }
}
