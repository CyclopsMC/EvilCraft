package evilcraft.api.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import evilcraft.api.config.configurable.ConfigurableBlockContainer;

/**
 * An extended {@link ItemBlockExtended} that will add the NBT data that is stored inside
 * the item to the placed {@link TileEntity} for the block.
 * Subinstances of {@link ConfigurableBlockContainer} will perform the inverse operation, being
 * that broken blocks will save the NBT data inside the dropped {@link ItemBlock}.
 * @author rubensworks
 *
 */
public class ItemBlockNBT extends ItemBlockExtended {
    
    /**
     * Make a new instance.
     * @param block The block instance.
     */
    public ItemBlockNBT(Block block) {
        super(block);
        this.setMaxStackSize(1);
    }
    
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
            TileEntity tile = world.getTileEntity(x, y, z);

            if (tile != null && stack.stackTagCompound != null) {
                tile.readFromNBT(stack.stackTagCompound);
            }

            return true;
        }

        return false;
    }

}
