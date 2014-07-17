package evilcraft.entities.tileentities;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import evilcraft.api.EntityHelpers;
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
	
	private EntityLivingBase spiritInstance = null;
	
	/**
	 * The name of the NBT tag that will hold spirit entity data.
	 */
	public static final String SPIRIT_TAG_NAME = "spiritTag";
	@NBTPersist
	private NBTTagCompound spiritTag = new NBTTagCompound();
	
    /**
     * Make a new instance.
     */
    public TileBoxOfEternalClosure() {
        
    }
    
    @Override
    public void updateEntity() {
        super.updateEntity();
        
        // TMP:
        if(getSpiritInstance() == null) {
        	setSpiritInstance(new VengeanceSpirit(getWorldObj()));
        }
        
        // TODO: check if frozen spirits in radius => select one and start sucking procedure
        // => set status filled
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
    	this.spiritInstance = spiritInstance;
    	if(spiritInstance != null) {
    		spiritInstance.writeToNBT(spiritTag);
    		String entityId = EntityList.getEntityString(spiritInstance);
    		spiritTag.setString(EntityHelpers.NBTTAG_ID, entityId);
    	}
    }
    
    /**
     * Get the spirit contained inside this box, could be null.
     * @return The contained spirit.
     */
    public EntityLivingBase getSpiritInstance() {
    	if(spiritInstance == null && !getWorldObj().isRemote) {
    		setSpiritInstance(getEntityFromNBT(getWorldObj(), spiritTag));
    	}
    	return spiritInstance;
    }

}
