package evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import evilcraft.block.ExcrementPile;
import evilcraft.block.ExcrementPileConfig;

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
        if(player.isSneaking()) {
            boolean done = false;
            int attempts = 0;
            while(attempts < ExcrementPileConfig.effectiveness) {
                done = ItemDye.applyBonemeal(itemStack.copy(), world, x, y, z, player) | done;
                attempts++;
            }
            if(done) {
                itemStack.stackSize--;
                if (!world.isRemote) {
                    world.playAuxSFX(2005, x, y, z, 0);
                }
                return true;
            }
        } else {
            if (block == ExcrementPile.getInstance() && itemStack != null) {
                if(ExcrementPile.canHeightenPileAt(world, x, y, z)) {
                    ExcrementPile.heightenPileAt(world, x, y, z);
                    itemStack.stackSize--;
                    return true;
                }
                return false;
            }
        }
        return super.onItemUse(itemStack, player, world, x, y, z, side, coordX, coordY, coordZ);
    }

}
