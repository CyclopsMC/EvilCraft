package evilcraft.api.item;

import evilcraft.api.entities.tileentitites.EvilCraftTileEntity;
import net.minecraft.nbt.NBTTagCompound;

/**
 * This is a temporary storage for NBT data when {@link EvilCraftTileEntity}s are destroyed.
 * In the dropped blocks method this tag should then be used to add to the dropped block.
 * @author rubensworks
 *
 */
public class TileEntityNBTStorage {

    /**
     * The temporary tag storage for dropped NBT data from {@link EvilCraftTileEntity}.
     */
    public static NBTTagCompound TAG = null;
    
}
