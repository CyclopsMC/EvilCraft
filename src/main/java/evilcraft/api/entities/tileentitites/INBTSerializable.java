package evilcraft.api.entities.tileentitites;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Objects that are serializable to NBT.
 * @author rubensworks
 *
 */
public interface INBTSerializable {

	/**
	 * Convert the data to an NBT tag.
	 * @return The NBT tag.
	 */
	public NBTTagCompound toNBT();
	/**
	 * Read the data from an NBT tag and place it in this object.
	 * @param tag The tag to read from.
	 */
	public void fromNBT(NBTTagCompound tag);
	
}
