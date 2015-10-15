package evilcraft.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.core.helper.EntityHelpers;
import evilcraft.core.helper.WorldHelpers;
import evilcraft.core.tileentity.EvilCraftTileEntity;
import evilcraft.core.tileentity.NBTPersist;
import evilcraft.entity.monster.VengeanceSpirit;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

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
	/**
	 * The name of the NBT tag that will hold the player id.
	 */
	public static final String NBTKEY_PLAYERID = "playerId";
	/**
	 * The name of the NBT tag that will hold the player name.
	 */
	public static final String NBTKEY_PLAYERNAME = "playerName";
	
	private static final int TICK_MODULUS = 10;
	private static final int TARGET_RADIUS = 10;
	private static final double ABSORB_RADIUS = 0.5D;
	private static final int NO_TARGET = -1;
	/**
	 * The lid angle for when this box is open.
	 */
	public static final float START_LID_ANGLE = 65F;
	private static final float LID_STEP = 11.5F;
	
	private EntityLivingBase spiritInstance = null;
	
	@NBTPersist
	private NBTTagCompound spiritTag = new NBTTagCompound();
	@NBTPersist
	private String playerId = "";
	@NBTPersist
	private String playerName = "";
	
	private VengeanceSpirit targetSpirit = null;
	
	@NBTPersist
	private Integer targetSpiritId = -1;
	
	@NBTPersist
	private Float lidAngle = START_LID_ANGLE;
	@NBTPersist
	private Boolean closing = true;
	private float previousLidAngle = 65F;
	
	private boolean initial = false;
	
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
    public void updateTileEntity() {
        super.updateTileEntity();
        
        innerRotation++;
        
        EntityLivingBase spirit = getSpiritInstance();
        VengeanceSpirit target = getTargetSpirit();
        if(spirit == null && !worldObj.isRemote) {
	        if(target != null
	        		|| (WorldHelpers.efficientTick(getWorldObj(), TICK_MODULUS, 
	        				xCoord, yCoord, zCoord) && findNextEntity())) {
	        	pullEntity();
	        }
        }
        
        if(worldObj.isRemote && target != null) {
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
    	VengeanceSpirit target = getTargetSpirit();
    	if(target != null) {
    		double dx = targetSpirit.posX - xCoord - 0.5D;
    		double dy = targetSpirit.posY - yCoord - 0.5D;
    		double dz = targetSpirit.posZ - zCoord - 0.5D;
    		double distance = (double)MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
    		
    		if(target.isDead || !target.isFrozen()) {
    			setTargetSpirit(null);
    		} else {
                if(target.boundingBox.expand(ABSORB_RADIUS, ABSORB_RADIUS, ABSORB_RADIUS).intersectsWith(getBlock().
                        getCollisionBoundingBoxFromPool(worldObj, xCoord, yCoord, zCoord))) {
	    			closing = true;
	    			close(true);
	    			setSpiritInstance(targetSpirit);
	    			worldObj.removeEntity(targetSpirit);
					this.playerId = targetSpirit.getPlayerId();
					this.playerName = targetSpirit.getPlayerName();
	    			setTargetSpirit(null);
	    		} else {
		    		double strength = (1D / (distance)) / 50D + 0.01D;
		    		target.motionX -= dx * strength;
		    		target.motionY -= dy * strength;
		    		target.motionZ -= dz * strength;
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
    	
    	if(lidAngle < 0) {
    		lidAngle = 0F;
    		updateLight();
    	}
    	if(lidAngle > START_LID_ANGLE) {
    		lidAngle = START_LID_ANGLE;
    		updateLight();
    	}
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
    			if(!initial) {
	    			getWorldObj().playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D,
	    					"random.chestclosed", 0.5F, getWorldObj().rand.nextFloat() * 0.1F + 0.9F);
    			}
    		}
    		sendUpdate();
    	}
    	initial = true;
    }
    
    protected void updateLight() {
    	worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
            EntityLivingBase entity = getEntityFromNBT(getWorldObj(), spiritTag);
            setSpiritInstance(entity);
            if(!VengeanceSpirit.canSustain(entity) && entity != null && !(entity instanceof VengeanceSpirit)) {
                releaseSpirit();
            }
            if(spiritInstance == null) {
                closing = false;
                close(true);
            }
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

    @Override
    protected void onSendUpdate() {
        super.onSendUpdate();
        // Trigger comparator update.
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, this.getBlock());
    }

}
