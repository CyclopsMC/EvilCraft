package evilcraft.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.blocks.ExcrementPile;
import evilcraft.blocks.ExcrementPileConfig;

/**
 * {@link ItemBlock} for the {@link ExcrementPile}.
 * @author rubensworks
 *
 */
public class ExcrementPileItemBlock extends ItemBlock {

    /**
     * Make a new instance.
     * @param blockID The block ID.
     */
    public ExcrementPileItemBlock(int blockID) {
        super(blockID);
    }
    
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float coordX, float coordY, float coordZ) {
        int block = world.getBlockId(x, y, z);
        if (block == ExcrementPileConfig._instance.ID && itemStack != null) {
            if(ExcrementPile.canHeightenPileAt(world, x, y, z)) {
                ExcrementPile.heightenPileAt(world, x, y, z);
                itemStack.stackSize--;
                return true;
            }
            return false;
        }
        return super.onItemUse(itemStack, player, world, x, y, z, side, coordX, coordY, coordZ);
    }

}
