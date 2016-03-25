package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float coordX, float coordY, float coordZ) {
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
                return EnumActionResult.SUCCESS;
            }
        } else {
            if (block == ExcrementPile.getInstance() && itemStack != null) {
                if(ExcrementPile.getInstance().canHeightenPileAt(world, blockPos)) {
                    ExcrementPile.getInstance().heightenPileAt(world, blockPos);
                    itemStack.stackSize--;
                    return EnumActionResult.SUCCESS;
                }
                return EnumActionResult.PASS;
            }
        }
        return super.onItemUse(itemStack, player, world, blockPos, hand, side, coordX, coordY, coordZ);
    }

}
