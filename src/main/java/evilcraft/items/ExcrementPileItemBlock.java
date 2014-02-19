package evilcraft.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.blocks.ExcrementPile;

/**
 * {@link ItemBlock} for the {@link ExcrementPile}.
 * @author rubensworks
 *
 */
public class ExcrementPileItemBlock extends ItemBlock {

    /**
     * Make a new instance.
     * @param block The block.
     */
    public ExcrementPileItemBlock(Block block) {
        super(block);
    }
    
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float coordX, float coordY, float coordZ) {
        Block block = world.getBlock(x, y, z);
        if (block == ExcrementPile.getInstance() && itemStack != null) {
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
