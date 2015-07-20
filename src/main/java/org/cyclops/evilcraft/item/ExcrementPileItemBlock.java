package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.cyclops.evilcraft.block.ExcrementPile;
import org.cyclops.evilcraft.block.ExcrementPileConfig;

/**
 * {@link ItemBlock} for the {@link ExcrementPile}.
 * @author rubensworks
 *
 */
public class ExcrementPileItemBlock extends ItemBlock {

    /**
     * Make a new instance.
     * @param block The blockState.
     */
    public ExcrementPileItemBlock(Block block) {
        super(block);
    }
    
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos blockPos, EnumFacing side, float coordX, float coordY, float coordZ) {
		Block block = world.getBlockState(blockPos).getBlock();
        if(player.isSneaking()) {
            boolean done = false;
            int attempts = 0;
            while(attempts < ExcrementPileConfig.effectiveness) {
                done = ItemDye.applyBonemeal(itemStack.copy(), world, blockPos, player) | done;
                attempts++;
            }
            if(done) {
                itemStack.stackSize--;
                if (!world.isRemote) {
                    world.playAuxSFX(2005, blockPos, 0);
                }
                return true;
            }
        } else {
            if (block == ExcrementPile.getInstance() && itemStack != null) {
                if(ExcrementPile.getInstance().canHeightenPileAt(world, blockPos)) {
                    ExcrementPile.getInstance().heightenPileAt(world, blockPos);
                    itemStack.stackSize--;
                    return true;
                }
                return false;
            }
        }
        return super.onItemUse(itemStack, player, world, blockPos, side, coordX, coordY, coordZ);
    }

}
