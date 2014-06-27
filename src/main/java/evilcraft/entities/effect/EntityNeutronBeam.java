package evilcraft.entities.effect;

import java.util.List;

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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.configurable.Configurable;
import evilcraft.entities.monster.VengeanceSpirit;
import evilcraft.render.particle.EntityBlurFX;

/**
 * Entity for the Neutron beams.
 * @author rubensworks
 *
 */
public class EntityNeutronBeam extends EntityThrowable implements Configurable{
	
	private static final int MAX_AGE = 20 * 20;
    
    protected ExtendedConfig<?> eConfig = null;
    private int age = 0;
    
    /**
     * The type for this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.ENTITY;

    @Override
    @SuppressWarnings("rawtypes")
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    /**
     * Make a new instance in the given world.
     * @param world The world to make it in.
     */
    public EntityNeutronBeam(World world) {
        super(world);
    }

    /**
     * Make a new instance in a world by a placer {@link EntityLivingBase}.
     * @param world The world.
     * @param entity The {@link EntityLivingBase} that placed this {@link Entity}.
     */
    public EntityNeutronBeam(World world, EntityLivingBase entity) {
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
    public EntityNeutronBeam(World world, double x, double y, double z) {
        super(world, x, y, z);
    }
    
    @Override
    protected float getGravityVelocity() {
        return 0.0F;
    }

    @Override
    public String getUniqueName() {
        return "entities.item."+eConfig.NAMEDID;
    }

    @Override
    public boolean isEntity() {
        return true;
    }
    
    @SuppressWarnings("rawtypes")
	@Override
    public void onUpdate() {
    	super.onUpdate();
    	Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
        Vec3 vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
        vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
        vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        
    	if (!this.worldObj.isRemote) {
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            EntityLivingBase entitylivingbase = this.getThrower();

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity)list.get(j);

                if (entity1 instanceof VengeanceSpirit) {
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
        	showNewBlurParticle();
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
            		VengeanceSpirit spirit = (VengeanceSpirit) position.entityHit;
            		spirit.onHit(posX, posY, posZ, motionX, motionY, motionZ);
            	}
            }
        }
        this.setDead();
    }

}
