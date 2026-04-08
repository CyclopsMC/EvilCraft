package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TintedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Glass that holds back some light.
 * @author rubensworks
 *
 */
public class BlockObscuredGlass extends TintedGlassBlock {

    public BlockObscuredGlass(Block.Properties properties) {
        super(properties);
    }

    @Override
    public int getLightDampening(BlockState state) {
        return 10;
    }
}
