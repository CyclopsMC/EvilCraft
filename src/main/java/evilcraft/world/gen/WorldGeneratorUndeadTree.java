package evilcraft.world.gen;

import evilcraft.block.UndeadLeaves;
import evilcraft.block.UndeadLogConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

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
    public boolean generate(World world, Random rand, int x, int retries, int z) {
        for(int c = 0; c < retries; c++) {
            int y = world.getActualHeight() - 1;
            while(world.isAirBlock(x, y, z) && y > 0) {
                y--;
            }

            if(!growTree(world, rand, x, y + 1, z)) {
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
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return If the tree was grown.
     */
    public boolean growTree(World world, Random rand, int x, int y, int z) {
        int treeHeight = rand.nextInt(9) + 4;
        int worldHeight = world.getHeight();
        Block block;

        if(y >= 1 && y + treeHeight + 1 <= worldHeight) {
            int blockId;
            int xOffset;
            int yOffset;
            int zOffset;

            block = world.getBlock(x, y - 1, z);

            if((block != null && block.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP,
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
                                block = world.getBlock(xOffset, yOffset, zOffset);

                                if(block != null && !(block.isLeaves(world, xOffset, yOffset, zOffset) ||
                                        block == Blocks.air ||
                                        block.canBeReplacedByLeaves(world, xOffset, yOffset, zOffset))) {
                                    return false;
                                }
                            }
                        }
                    } else {
                        return false;
                    }
                }

                block = world.getBlock(x, y - 1, z);
                if (block != null) {
                    block.onPlantGrow(world, x, y - 1, z, x, y, z);

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
    
                                block = world.getBlock(xOffset, yOffset, zOffset);
    
                                if(((xPos != center | zPos != center) ||
                                        rand.nextInt(2) != 0 && var12 != 0) &&
                                        (block == null || block.isLeaves(world, xOffset, yOffset, zOffset) ||
                                        block == Blocks.air ||
                                        block.canBeReplacedByLeaves(world, xOffset, yOffset, zOffset))) {
                                    this.setBlockAndNotifyAdequately(world, xOffset, yOffset, zOffset,
                                            leaves, 0);
                                }
                            }
                        }
                    }

                    // Replace replacable blocks with logs
                    for(yOffset = 0; yOffset < treeHeight; ++yOffset) {
                        block = world.getBlock(x, y + yOffset, z);

                        if(block == null || block == Blocks.air  ||
                                block.isLeaves(world, x, y + yOffset, z) ||
                                block.isReplaceable(world, x, y + yOffset, z)) {
                            this.setBlockAndNotifyAdequately(world, x, y + yOffset, z,
                                    logs, 0);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
