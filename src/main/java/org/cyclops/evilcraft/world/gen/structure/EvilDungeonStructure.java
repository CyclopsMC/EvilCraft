package org.cyclops.evilcraft.world.gen.structure;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.DungeonHooks;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.Configs;
import org.cyclops.evilcraft.block.BloodyCobblestoneConfig;

import java.util.Random;

/**
 * Evil Dungeon, based on vanilla dungeons
 * @author rubensworks
 *
 */
public class EvilDungeonStructure extends WorldGenDungeons {
    
    private static final int RADIUS_X = 3;
    private static final int RADIUS_X_RAND = 4;
    private static final int RADIUS_Z = 3;
    private static final int RADIUS_Z_RAND = 4;
    private static final int CHESTS = 2;
    private static final int CHESTS_RAND = 2;

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
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
                    // chunkExists does not exist in 1.9 anymore
                	if(!((ChunkProviderServer) world.getChunkProvider()).chunkExists(xr / 16, yr / 16)) {
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
                            world.setBlockToAir(loopPos);
                        } else if (yr >= 0 && !world.getBlockState(loopPos.add(0, -1, 0)).getMaterial().isSolid()) {
                            world.setBlockToAir(loopPos);
                        } else if (world.getBlockState(loopPos).getMaterial().isSolid()) {
                            if (yr == y - 1 && random.nextInt(4) != 0) {
								if(Configs.isEnabled(BloodyCobblestoneConfig.class)) {
									world.setBlockState(loopPos, BloodyCobblestoneConfig._instance.getBlockInstance().getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
								}
                            } else {
                                world.setBlockState(loopPos, Blocks.cobblestone.getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
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
                        world.setBlockState(loopPos, Blocks.chest.getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                        TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(loopPos);

                        if (tileentitychest != null) {
                            tileentitychest.setLoot(LootTableList.CHESTS_SPAWN_BONUS_CHEST, world.rand.nextLong());
                        }

                        chests--;
                    }
                }
                ++xr;
            }

            for(int xs = x - 1; xs <= x + 1; xs += 2) {
                for(int zs = z - 1; zs <= z + 1; zs += 2) {
                    BlockPos loopPos = new BlockPos(xs, y, zs);
                    world.setBlockState(loopPos, Blocks.mob_spawner.getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                    TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)world.getTileEntity(loopPos);
        
                    if (tileentitymobspawner != null) {
                        tileentitymobspawner.getSpawnerBaseLogic().setEntityName(this.pickMobSpawner(random));
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
    
    /**
     * Randomly decides which spawner to use in a dungeon
     */
    private String pickMobSpawner(Random par1Random)
    {
        return DungeonHooks.getRandomDungeonMob(par1Random);
    }

}
