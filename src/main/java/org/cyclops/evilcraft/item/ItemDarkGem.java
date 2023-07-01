package org.cyclops.evilcraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodStain;
import org.cyclops.evilcraft.block.BlockDarkOre;
import org.cyclops.evilcraft.block.BlockFluidBlood;

import javax.annotation.Nullable;

/**
 * Gem that drops from {@link BlockDarkOre}.
 * @author rubensworks
 *
 */
public class ItemDarkGem extends Item {

    private static final int REQUIRED_BLOOD_BLOCKS = 5;
    private static final int TICK_MODULUS = 5;

    public ItemDarkGem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem) {
        // This will transform a dark gem into a blood infusion core when it finds
        // REQUIRED_BLOOD_BLOCKS blood fluid blocks in the neighbourhood.
        if (!entityItem.level().isClientSide()
                && WorldHelpers.efficientTick(entityItem.level(), TICK_MODULUS,
                        (int) entityItem.getX(), (int) entityItem.getY(), (int) entityItem.getZ())) {
            final BlockPos blockPos = entityItem.blockPosition();
            Level world = entityItem.level();

            int amount = 0;
            if(isValidBlock(world, blockPos)) {
                // For storing REQUIRED_BLOOD_BLOCKS coordinates
                final BlockPos[] visited = new BlockPos[REQUIRED_BLOOD_BLOCKS];

                // Save first coordinate
                visited[amount] = blockPos;
                amount++;

                // Search in neighbourhood
                WorldHelpers.foldArea(world, 3, blockPos, new WorldHelpers.WorldFoldingFunction<Integer, Integer, Level>() {
                    @Nullable
                    @Override
                    public Integer apply(@Nullable Integer amount, Level world, BlockPos pos) {
                        if(amount == null || amount == -1) return amount;
                        if(!(pos.getX() == blockPos.getX() && pos.getY() == blockPos.getY() && pos.getZ() == blockPos.getZ()) && isValidBlock(world, pos)) {
                            // Save next coordinate
                            visited[amount] = pos;

                            // Do the transform when REQUIRED_BLOOD_BLOCKS are found
                            if(++amount == REQUIRED_BLOOD_BLOCKS) {
                                // Spawn the new item
                                entityItem.getItem().shrink(1);
                                entityItem.spawnAtLocation(new ItemStack(RegistryEntries.ITEM_DARK_POWER_GEM));

                                // Retrace coordinate steps and remove all those blocks + spawn particles
                                for(int restep = 0; restep < amount; restep++) {
                                    world.setBlockAndUpdate(visited[restep], Blocks.AIR.defaultBlockState());
                                    if (world.isClientSide())
                                        BlockBloodStain.splash(world, visited[restep].offset(0, -1, 0));
                                    world.updateNeighborsAt(visited[restep], Blocks.AIR);
                                }
                                return -1;
                            }
                        }
                        return amount;
                    }
                }, amount);
            }
        }
        return false;
    }

    private boolean isValidBlock(Level world, BlockPos blockPos) {
        // Not working: world.getFluidState(blockPos).getFluid() == RegistryEntries.FLUID_BLOOD
        return world.getBlockState(blockPos).getBlock() instanceof BlockFluidBlood
                && world.getFluidState(blockPos).isSource();
    }

}
