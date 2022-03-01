package org.cyclops.evilcraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockInvisibleRedstone;
import org.cyclops.evilcraft.item.ItemRedstoneGrenadeConfig;

/**
 * Tile for the {@link BlockInvisibleRedstone}.
 * @author rubensworks
 *
 */
public class BlockEntityInvisibleRedstone extends CyclopsBlockEntity {

    // Destroy redstone blockState after 1 redstone tick (= 2 game ticks)
    private static final int TICK_DESTRUCTION_THRESHOLD = 2;

    protected int destroyCountDown;

    public BlockEntityInvisibleRedstone(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_INVISIBLE_REDSTONE, blockPos, blockState);
        destroyCountDown = TICK_DESTRUCTION_THRESHOLD;
    }

    public static class TickerServer extends BlockEntityTickerDelayed<BlockEntityInvisibleRedstone> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityInvisibleRedstone blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            if (blockEntity.destroyCountDown-- <= 0) {
                level.removeBlock(pos, false);
                if(ItemRedstoneGrenadeConfig.dropAfterUsage) {
                    ItemStackHelpers.spawnItemStack(level, pos, new ItemStack(RegistryEntries.ITEM_REDSTONE_GRENADE));
                }
            }
        }
    }
}
