package evilcraft.entity.effect;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Achievements;
import evilcraft.EvilCraft;
import evilcraft.client.particle.EntityBlurFX;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.entity.monster.VengeanceSpirit;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

/**
 * Entity for the anti-vengeance beams.
 * @author rubensworks
 *
 */
public class EntityAntiVengeanceBeam extends EntityThrowable implements IConfigurable{
	
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
    	Vec3 vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
        vec3 = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec31 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        
        soundTick++;
        if(soundTick > 3 && this.getEntityId() % 10 == 0) {
        	soundTick = 0;
        }
        
    	if (!this.worldObj.isRemote) {
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity)list.get(j);

                if (entity1 instanceof VengeanceSpirit && !((VengeanceSpirit) entity1).isSwarm()) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

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
                movingobjectposition = new MovingObjectPosition(entity);
            }
        } else {
        	for(int i = 0; i < worldObj.rand.nextInt(5) + 5; i++) {
        		showNewBlurParticle();
        	}
        	if(soundTick == 1) {
	        	// Play beam sound
	        	EvilCraft.proxy.playSound(posX, posY, posZ,
	        			"vengeanceBeam", 0.5F + worldObj.rand.nextFloat() * 0.2F, 1.0F);
        	}
        }
    	
    	if (movingobjectposition != null) {
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal) {
                this.setInPortal();
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
    protected void onImpact(MovingObjectPosition position) {
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

}
