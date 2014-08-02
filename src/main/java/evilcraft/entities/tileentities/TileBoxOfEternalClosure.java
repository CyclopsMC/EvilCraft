package evilcraft.entities.tileentities;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.api.EntityHelpers;
import evilcraft.api.Helpers;
import evilcraft.api.entities.tileentitites.EvilCraftTileEntity;
import evilcraft.api.entities.tileentitites.NBTPersist;
import evilcraft.entities.monster.VengeanceSpirit;

/**
 * A chest that is able to repair tools with the use of blood.
 * Partially based on cpw's IronChests.
 * @author rubensworks
 *
 */
public class TileBoxOfEternalClosure extends EvilCraftTileEntity {
	
	/**
	 * The name of the NBT tag that will hold spirit entity data.
	 */
	public static final String NBTKEY_SPIRIT = "spiritTag";
	
	private static final int TICK_MODULUS = 10;
	private static final int TARGET_RADIUS = 10;
	private static final double ABSORB_RADIUS = 1D;
	private static final int NO_TARGET = -1;
	/**
	 * The lid angle for when this box is open.
	 */
	public static final float START_LID_ANGLE = 65F;
	private static final float LID_STEP = 11.5F;
	
	private EntityLivingBase spiritInstance = null;
	
	@NBTPersist
	private NBTTagCompound spiritTag = new NBTTagCompound();
	
	private VengeanceSpirit targetSpirit = null;
	
	@NBTPersist
	private Integer targetSpiritId = -1;
	
	@NBTPersist
	private Float lidAngle = START_LID_ANGLE;
	@NBTPersist
	private Boolean closing = true;
	private float previousLidAngle = 65F;
	
	/**
	 * Inner rotation of a beam.
	 */
	public int innerRotation;
	
    /**
     * Make a new instance.
     */
    public TileBoxOfEternalClosure() {
    	innerRotation = new Random().nextInt(100000);
    }
    
    @Override
    public void updateEntity() {
        super.updateEntity();
        
        innerRotation++;
        
        if(getSpiritInstance() == null && !worldObj.isRemote) {
	        if(targetSpirit != null
	        		|| (Helpers.efficientTick(getWorldObj(), TICK_MODULUS) && findNextEntity())) {
	        	pullEntity();
	        }
        }
        
        if(worldObj.isRemote && getTargetSpirit() != null) {
        	EvilCraft.proxy.playSound(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D,
        			"boxBeam", 0.1F + worldObj.rand.nextFloat() * 0.9F,
        			0.1F + worldObj.rand.nextFloat() * 0.9F);
        }
        
        close(false);
    }
    
    private boolean findNextEntity() {
    	AxisAlignedBB box = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord,
    			xCoord, yCoord, zCoord).expand(TARGET_RADIUS, TARGET_RADIUS, TARGET_RADIUS);
    	@SuppressWarnings("unchecked")
		List<VengeanceSpirit> entities = worldObj.getEntitiesWithinAABB(VengeanceSpirit.class, box);
    	double minDistance = TARGET_RADIUS + 1;
    	VengeanceSpirit closest = null;
    	for(VengeanceSpirit spirit : entities) {
    		if(spirit.isFrozen() && !spirit.isSwarm()) {
	    		double distance = spirit.getDistance(xCoord, yCoord, zCoord);
	    		if(distance < minDistance) {
	    			minDistance = distance;
	    			closest = spirit;
	    		}
    		}
    	}
    	setTargetSpirit(closest);
    	return targetSpirit != null;
    }
    
    private void pullEntity() {
    	if(getTargetSpirit() != null) {
    		double dx = targetSpirit.posX - xCoord - 0.5D;
    		double dy = targetSpirit.posY - yCoord - 0.5D;
    		double dz = targetSpirit.posZ - zCoord - 0.5D;
    		double distance = (double)MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
    		
    		if(getTargetSpirit().isDead || !getTargetSpirit().isFrozen()) {
    			setTargetSpirit(null);
    		} else {
	    		if(distance <= ABSORB_RADIUS) {
	    			closing = true;
	    			close(true);
	    			setSpiritInstance(targetSpirit);
	    			worldObj.removeEntity(targetSpirit);
	    			setTargetSpirit(null);
	    		} else {
		    		double strength = (1D / (distance)) / 50D + 0.01D;
		    		targetSpirit.motionX -= dx * strength;
		    		targetSpirit.motionY -= dy * strength;
		    		targetSpirit.motionZ -= dz * strength;
	    		}
    		}
    	}
    }
    
    private void close(boolean force) {
    	previousLidAngle = lidAngle;
    	if((lidAngle > 0 && lidAngle < START_LID_ANGLE) || force) {
    		if(closing) {
    			lidAngle -= LID_STEP;
    		} else {
    			lidAngle += LID_STEP;
    		}
    	}
    	
    	if(lidAngle < 0) lidAngle = 0F;
    	if(lidAngle > START_LID_ANGLE) lidAngle = START_LID_ANGLE;
    }
    
    /**
     * Release the inner spirit into the world.
     */
    public void releaseSpirit() {
    	VengeanceSpirit spirit = new VengeanceSpirit(getWorldObj());
    	spirit.copyDataFrom((VengeanceSpirit) spiritInstance, true);
    	Random rand = worldObj.rand;
    	spirit.setPosition(xCoord + rand.nextDouble(), yCoord + rand.nextDouble(),
    			zCoord + rand.nextDouble());
    	spirit.setFrozenDuration(0);
    	spirit.setGlobalVengeance(true);
    	spirit.setRemainingLife(MathHelper.getRandomIntegerInRange(worldObj.rand,
    			VengeanceSpirit.REMAININGLIFE_MIN, VengeanceSpirit.REMAININGLIFE_MAX));
    	
    	closing = false;
    	close(true);
    	this.clearSpiritInstance();
    	
    	worldObj.spawnEntityInWorld(spirit);
        spirit.onSpawnWithEgg((IEntityLivingData)null);
    }
    
    /**
     * Get the entity from a tag.
     * @param world The world the entity should live in.
     * @param tag The tag holding the entity data.
     * @return The entity instance.
     */
    public static EntityLivingBase getEntityFromNBT(World world, NBTTagCompound tag) {
    	if(tag != null && tag.hasKey(EntityHelpers.NBTTAG_ID)) {
			return (EntityLivingBase) EntityList
					.createEntityFromNBT(tag, world);
		}
    	return null;
    }
    
    protected void setSpiritInstance(EntityLivingBase spiritInstance) {
    	EntityLivingBase old = this.spiritInstance;
    	this.spiritInstance = spiritInstance;
    	if(spiritInstance != null) {
    		spiritInstance.writeToNBT(spiritTag);
    		String entityId = EntityList.getEntityString(spiritInstance);
    		spiritTag.setString(EntityHelpers.NBTTAG_ID, entityId);
    	}
    	
    	if(old != spiritInstance) {
    		if(old == null) {
    			getWorldObj().playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D,
    					"random.chestclosed", 0.5F, getWorldObj().rand.nextFloat() * 0.1F + 0.9F);
    		}
    		sendUpdate();
    	}
    }
    
    protected void clearSpiritInstance() {
    	EntityLivingBase old = this.spiritInstance;
    	this.spiritInstance = null;
    	spiritTag = new NBTTagCompound();
    	
    	if(old != null) {
    		sendUpdate();
    	}
    }
    
    /**
     * Get the spirit contained inside this box, could be null.
     * @return The contained spirit.
     */
    public EntityLivingBase getSpiritInstance() {
    	if(spiritInstance == null) {
    		setSpiritInstance(getEntityFromNBT(getWorldObj(), spiritTag));
    	}
    	return spiritInstance;
    }

	/**
	 * @return the lidAngle
	 */
	public float getLidAngle() {
		return lidAngle;
	}

	/**
	 * @return the previousLidAngle
	 */
	public float getPreviousLidAngle() {
		return previousLidAngle;
	}
	
	/**
	 * Get the target spirit.
	 * @return The target spirit.
	 */
	public VengeanceSpirit getTargetSpirit() {
		// Make sure our target spirit is up-to-date with the server-synced target spirit ID.
		if(getWorldObj().isRemote && targetSpiritId == NO_TARGET) {
			targetSpirit = null;
		} else if(targetSpirit == null && targetSpiritId != NO_TARGET) {
			setTargetSpirit((VengeanceSpirit)getWorldObj().getEntityByID(targetSpiritId));
		}
		return targetSpirit;
	}

	protected void setTargetSpirit(VengeanceSpirit targetSpirit) {
		VengeanceSpirit old = this.targetSpirit;
		this.targetSpirit = targetSpirit;
    	if(targetSpirit != null) {
    		targetSpiritId = targetSpirit.getEntityId();
    	} else {
    		targetSpiritId = NO_TARGET;
    	}
    	
    	if(old != targetSpirit) {
    		sendUpdate();
    	}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
		return TileEntity.INFINITE_EXTENT_AABB;
	}

}
