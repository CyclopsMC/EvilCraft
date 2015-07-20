package org.cyclops.evilcraft.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.cyclops.evilcraft.block.UndeadLeaves;
import org.cyclops.evilcraft.block.UndeadLogConfig;

import java.util.Random;

/**
 * WorldGenerator for Undead Trees.
 * Inspired by MFR tree generator
 * @author rubensworks
 *
 */
public class WorldGeneratorUndeadTree extends WorldGenerator {

    private Block sapling = null;
    private Block leaves = UndeadLeaves.getInstance();
    private Block logs = UndeadLogConfig._instance.getBlockInstance();

    /**
     * Make a new instance.
     */
    public WorldGeneratorUndeadTree() {
        super();
    }

    /**
     * Make a new instance.
     * @param doNotify If the generator should notify the world.
     * @param sapling The sapling for this tree.
     */
    public WorldGeneratorUndeadTree(boolean doNotify, Block sapling) {
        super(doNotify);
        this.sapling = sapling;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos blockPos) {
        int x = blockPos.getX();
        int retries = blockPos.getY();
        int z = blockPos.getZ();
        for(int c = 0; c < retries; c++) {
            int y = world.getActualHeight() - 1;
            BlockPos loopPos = new BlockPos(x, y, z);
            while(world.isAirBlock(loopPos) && y > 0) {
                y--;
            }

            if(!growTree(world, rand, loopPos.add(0, 1, 0))) {
                retries--;
            }

            x += rand.nextInt(16) - 8;
            z += rand.nextInt(16) - 8;
        }

        return true;
    }

    /**
     * Grow an Undead Tree at the given location.
     * @param world The world.
     * @param rand Random object.
     * @param blockPos The position.
     * @return If the tree was grown.
     */
    public boolean growTree(World world, Random rand, BlockPos blockPos) {
        int treeHeight = rand.nextInt(9) + 4;
        int worldHeight = world.getHeight();
        Block block;

        if(blockPos.getY() >= 1 && blockPos.getY() + treeHeight + 1 <= worldHeight) {
            int xOffset;
            int yOffset;
            int zOffset;

            BlockPos basePos = blockPos.add(0, -1, 0);
            block = world.getBlockState(basePos).getBlock();
            int x = blockPos.getX();
            int y = blockPos.getY();
            int z = blockPos.getZ();

            if((block != null && block.canSustainPlant(world, basePos, EnumFacing.UP,
                    ((BlockSapling)sapling))) && y < worldHeight - treeHeight - 1) {
                for(yOffset = y; yOffset <= y + 1 + treeHeight; ++yOffset) {
                    byte radius = 1;

                    if(yOffset == y) {
                        radius = 0;
                    }

                    if(yOffset >= y + 1 + treeHeight - 3) {
                        radius = 3;
                    }

                    // Check if leaves can be placed
                    if(yOffset >= 0 & yOffset < worldHeight) {
                        for(xOffset = x - radius; xOffset <= x + radius; ++xOffset) {
                            for(zOffset = z - radius; zOffset <= z + radius; ++zOffset) {
                                BlockPos loopPos = new BlockPos(xOffset, yOffset, zOffset);
                                block = world.getBlockState(loopPos).getBlock();

                                if(block != null && !(block.isLeaves(world, loopPos) ||
                                        block == Blocks.air ||
                                        block.canBeReplacedByLeaves(world, loopPos))) {
                                    return false;
                                }
                            }
                        }
                    } else {
                        return false;
                    }
                }

                block = world.getBlockState(basePos).getBlock();
                if (block != null) {
                    block.onPlantGrow(world, basePos, blockPos);

                    // Add leaves
                    for(yOffset = y - 3 + treeHeight; yOffset <= y + treeHeight; ++yOffset) {
                        int var12 = yOffset - (y + treeHeight);
                        int center = 1 - var12 / 2;

                        for(xOffset = x - center; xOffset <= x + center; ++xOffset) {
                            int xPos = xOffset - x;
                            int t = xPos >> 31;
                            xPos = (xPos + t) ^ t;

                            for(zOffset = z - center; zOffset <= z + center; ++zOffset) {
                                int zPos = zOffset - z;
                                zPos = (zPos + (t = zPos >> 31)) ^ t;
                                BlockPos loopPos = new BlockPos(xOffset, yOffset, zOffset);
    
                                block = world.getBlockState(loopPos).getBlock();
    
                                if(((xPos != center | zPos != center) ||
                                        rand.nextInt(2) != 0 && var12 != 0) &&
                                        (block == null || block.isLeaves(world, loopPos) ||
                                        block == Blocks.air ||
                                        block.canBeReplacedByLeaves(world, loopPos))) {
                                    this.setBlockAndNotifyAdequately(world, loopPos, leaves.getDefaultState());
                                }
                            }
                        }
                    }

                    // Replace replacable blocks with logs
                    for(yOffset = 0; yOffset < treeHeight; ++yOffset) {
                        BlockPos loopPos = blockPos.add(0, yOffset, z);

                        if(block == null || block == Blocks.air  ||
                                block.isLeaves(world, loopPos) ||
                                block.isReplaceable(world, loopPos)) {
                            this.setBlockAndNotifyAdequately(world, loopPos, logs.getDefaultState());
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
