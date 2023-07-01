package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.List;

/**
 * A hardened version of blood.
 * @author rubensworks
 *
 */
public class BlockHardenedBlood extends Block {

    public BlockHardenedBlood(Block.Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        if (drops.isEmpty()) {
            ServerLevel world = builder.getLevel();
            BlockPos blockPos = BlockPos.containing(builder.getOptionalParameter(LootContextParams.ORIGIN));
            BlockState blockState = world.getBlockState(blockPos.offset(0, -1, 0));

            if (blockState.blocksMotion() || blockState.liquid()) {
                world.setBlockAndUpdate(blockPos, RegistryEntries.BLOCK_BLOOD.defaultBlockState());
            }
        }
        return drops;
    }

    @Override
    public void handlePrecipitation(BlockState blockState, Level world, BlockPos blockPos, Biome.Precipitation precipitation) {
        world.setBlockAndUpdate(blockPos, RegistryEntries.BLOCK_BLOOD.defaultBlockState());
    }
}
