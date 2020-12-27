package org.cyclops.evilcraft.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.loot.LootTables;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.DungeonsFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Random;

/**
 * Evil Dungeon, based on vanilla dungeons
 * @author rubensworks
 *
 */
public class WorldFeatureEvilDungeon extends DungeonsFeature {
    
    private static final int RADIUS_X = 3;
    private static final int RADIUS_X_RAND = 4;
    private static final int RADIUS_Z = 3;
    private static final int RADIUS_Z_RAND = 4;
    private static final int CHESTS = 2;
    private static final int CHESTS_RAND = 2;

    public WorldFeatureEvilDungeon(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos blockPos, NoFeatureConfig config) {
        int height = 3;
        int radiusX = random.nextInt(RADIUS_X_RAND) + RADIUS_X;
        int radiusZ = random.nextInt(RADIUS_Z_RAND) + RADIUS_Z;
        int chests = random.nextInt(CHESTS) + CHESTS_RAND;
        int openingCounter = 0; // Counts the amount of 'holes' that would be available when the dungeon should be placed

        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        // Check if this is a valid spot for a dungeon
        for (int xr = x - radiusX - 1; xr <= x + radiusX + 1; ++xr) {
            for (int yr = y - 1; yr <= y + height + 1; ++yr) {
                for (int zr = z - radiusZ - 1; zr <= z + radiusZ + 1; ++zr) {
                	BlockPos loopPos = new BlockPos(xr, yr, zr);

                	// Skip invalid chunk generation positions.
                	if(!world.getChunkProvider().chunkExists(xr / 16, yr / 16)) {
                		return false;
                	}
                	
                    Material material = world.getBlockState(loopPos).getMaterial();
                    if (yr == y - 1 && !material.isSolid())
                        return false;
                    if (yr == y + height + 1 && !material.isSolid())
                        return false;
                    if ((xr == x - radiusX - 1 || xr == x + radiusX + 1 || zr == z - radiusZ - 1 || zr == z + radiusZ + 1)
                            && yr == y && world.isAirBlock(loopPos) && world.isAirBlock(loopPos.add(0, 1, 0)))
                        ++openingCounter;
                }
            }
        }

        if (openingCounter >= 1 && openingCounter <= 15) {
            for (int xr = x - radiusX - 1; xr <= x + radiusX + 1; ++xr) {
                for (int yr = y + height; yr >= y - 1; --yr) {
                    for (int zr = z - radiusZ - 1; zr <= z + radiusZ + 1; ++zr) {
                        BlockPos loopPos = new BlockPos(xr, yr, zr);
                        if (xr != x - radiusX - 1
                                && yr != y - 1
                                && zr != z - radiusZ - 1
                                && xr != x + radiusX + 1
                                && yr != y + height + 1
                                && zr != z + radiusZ + 1) {
                            world.removeBlock(loopPos, false);
                        } else if (yr >= 0 && !world.getBlockState(loopPos.add(0, -1, 0)).getMaterial().isSolid()) {
                            world.removeBlock(loopPos, false);
                        } else if (world.getBlockState(loopPos).getMaterial().isSolid()) {
                            if (yr == y - 1 && random.nextInt(4) != 0) {
                                world.setBlockState(loopPos, RegistryEntries.BLOCK_BLOODY_COBBLESTONE.getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                            } else {
                                world.setBlockState(loopPos, Blocks.COBBLESTONE.getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                            }
                        }
                    }
                }
            }

            int attempts = 100;
            int xr = 0;
            while (xr < attempts && chests > 0) {
                int xrr = x + random.nextInt(radiusX * 2 + 1) - radiusX;
                int zrr = z + random.nextInt(radiusZ * 2 + 1) - radiusZ;
                BlockPos loopPos = new BlockPos(xrr, y, zrr);

                if (world.isAirBlock(loopPos)) {
                    int wallCounter = 0;

                    if (world.getBlockState(loopPos.add(-1, 0, 0)).getMaterial().isSolid())
                        ++wallCounter;

                    if (world.getBlockState(loopPos.add(1, 0, 0)).getMaterial().isSolid())
                        ++wallCounter;

                    if (world.getBlockState(loopPos.add(0, 0, -1)).getMaterial().isSolid())
                        ++wallCounter;

                    if (world.getBlockState(loopPos.add(0, 0, 1)).getMaterial().isSolid())
                        ++wallCounter;

                    if (wallCounter == 1) {
                        world.setBlockState(loopPos, Blocks.CHEST.getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                        ChestTileEntity tileentitychest = (ChestTileEntity)world.getTileEntity(loopPos);

                        if (tileentitychest != null) {
                            tileentitychest.setLootTable(LootTables.CHESTS_SIMPLE_DUNGEON, random.nextLong());
                        }

                        chests--;
                    }
                }
                ++xr;
            }

            for(int xs = x - 1; xs <= x + 1; xs += 2) {
                for(int zs = z - 1; zs <= z + 1; zs += 2) {
                    BlockPos loopPos = new BlockPos(xs, y, zs);
                    world.setBlockState(loopPos, Blocks.SPAWNER.getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                    MobSpawnerTileEntity tileentity = (MobSpawnerTileEntity)world.getTileEntity(loopPos);
        
                    if (tileentity != null) {
                        tileentity.getSpawnerBaseLogic().setEntityType(net.minecraftforge.common.DungeonHooks.getRandomDungeonMob(random));
                    } else {
                        System.err.println("Failed to fetch mob spawner entity at (" + xs + ", " + y + ", " + zs + ")");
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

}
