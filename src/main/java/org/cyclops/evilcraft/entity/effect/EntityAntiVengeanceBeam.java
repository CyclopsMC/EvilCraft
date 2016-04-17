package org.cyclops.evilcraft.entity.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.evilcraft.Achievements;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.EvilCraftSoundEvents;
import org.cyclops.evilcraft.client.particle.EntityBlurFX;
import org.cyclops.evilcraft.entity.monster.VengeanceSpirit;

import java.util.List;

/**
 * Entity for the anti-vengeance beams.
 * @author rubensworks
 *
 */
public class EntityAntiVengeanceBeam extends EntityThrowable implements IConfigurable {
	
	private static final int MAX_AGE = 10 * 20;
    
    private int age = 0;
    private int soundTick = 0;
    
    /**
     * Make a new instance in the given world.
     * @param world The world to make it in.
     */
    public EntityAntiVengeanceBeam(World world) {
        super(world);
    }

    /**
     * Make a new instance in a world by a placer {@link EntityLivingBase}.
     * @param world The world.
     * @param entity The {@link EntityLivingBase} that placed this {@link Entity}.
     */
    public EntityAntiVengeanceBeam(World world, EntityLivingBase entity) {
        super(world, entity);
        this.motionX /= 4;
        this.motionY /= 4;
        this.motionZ /= 4;
    }
    
    /**
     * Make a new instance at the given location in a world.
     * @param world The world.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    @SideOnly(Side.CLIENT)
    public EntityAntiVengeanceBeam(World world, double x, double y, double z) {
        super(world, x, y, z);
    }
    
    @Override
    protected float getGravityVelocity() {
        return 0.0F;
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    public void onUpdate() {
    	Vec3d vec3 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec31 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
        vec3 = new Vec3d(this.posX, this.posY, this.posZ);
        vec31 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        
        soundTick++;
        if(soundTick > 3 && this.getEntityId() % 10 == 0) {
        	soundTick = 0;
        }
        
    	if (!this.worldObj.isRemote) {
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            for (Object element : list) {
                Entity entity1 = (Entity) element;

                if (entity1 instanceof VengeanceSpirit && !((VengeanceSpirit) entity1).isSwarm()) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f, (double) f, (double) f);
                    RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new RayTraceResult(entity);
            }
        } else {
        	for(int i = 0; i < worldObj.rand.nextInt(5) + 5; i++) {
        		showNewBlurParticle();
        	}
        	if(soundTick == 1) {
	        	// Play beam sound
	        	EvilCraft.proxy.playSound(posX, posY, posZ,
                        EvilCraftSoundEvents.effect_vengeancebeam_base, SoundCategory.NEUTRAL,
                        0.5F + worldObj.rand.nextFloat() * 0.2F, 1.0F);
        	}
        }
    	
    	if (movingobjectposition != null) {
            if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK && this.worldObj.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.portal) {
                this.inPortal = true;
            } else {
                this.onImpact(movingobjectposition);
            }
        }
    	
    	if(age++ > MAX_AGE) {
    		this.setDead();
    	}
    	
    	super.onUpdate();
    }

    @SideOnly(Side.CLIENT)
    private void showNewBlurParticle() {
    	float scale = 0.6F - rand.nextFloat() * 0.3F;
    	float red = rand.nextFloat() * 0.03F + 0.01F;
        float green = rand.nextFloat() * 0.03F;
        float blue = rand.nextFloat() * 0.05F + 0.05F;
        float ageMultiplier = (float) (rand.nextDouble() * 6.5D + 4D); 
        
		EntityBlurFX blur = new EntityBlurFX(worldObj, posX, posY, posZ, scale,
				deriveMotion(motionX), deriveMotion(motionY), deriveMotion(motionZ),
				red, green, blue, ageMultiplier);
		Minecraft.getMinecraft().effectRenderer.addEffect(blur);
	}
    
    private double deriveMotion(double motion) {
    	return motion * 0.5D + (0.02D - rand.nextDouble() * 0.04D);
    }

	@Override
    protected void onImpact(RayTraceResult position) {
        if (!this.worldObj.isRemote) {
            if (this.getThrower() != null && this.getThrower() instanceof EntityPlayerMP) {
            	if(position.entityHit != null && position.entityHit instanceof VengeanceSpirit) {
            		((EntityPlayerMP) this.getThrower()).addStat(Achievements.CLOSURE, 1);
            		VengeanceSpirit spirit = (VengeanceSpirit) position.entityHit;
            		spirit.onHit(posX, posY, posZ, motionX, motionY, motionZ);
            	}
            }
        }
        this.setDead();
    }

    @Override
    public ExtendedConfig<?> getConfig() {
        return null;
    }

}
