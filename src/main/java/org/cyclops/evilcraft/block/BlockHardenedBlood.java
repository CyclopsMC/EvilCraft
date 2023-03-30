package org.cyclops.evilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
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
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        if (drops.isEmpty()) {
            ServerLevel world = builder.getLevel();
            BlockPos blockPos = BlockPos.containing(builder.getOptionalParameter(LootContextParams.ORIGIN));
            Material material = world.getBlockState(blockPos.offset(0, -1, 0)).getMaterial();

            if (material.blocksMotion() || material.isLiquid()) {
                world.setBlockAndUpdate(blockPos, RegistryEntries.BLOCK_BLOOD.defaultBlockState());
            }
        }
        return drops;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.NORMAL;
    }

    @Override
    public void handlePrecipitation(BlockState blockState, Level world, BlockPos blockPos, Biome.Precipitation precipitation) {
        world.setBlockAndUpdate(blockPos, RegistryEntries.BLOCK_BLOOD.defaultBlockState());
    }
}
