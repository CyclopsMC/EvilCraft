package evilcraft.world.gen.structure;

import evilcraft.Configs;
import evilcraft.block.BloodyCobblestoneConfig;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DungeonHooks;

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
    public boolean generate(World world, Random random, int x, int y, int z) {
        int height = 3;
        int radiusX = random.nextInt(RADIUS_X_RAND) + RADIUS_X;
        int radiusZ = random.nextInt(RADIUS_Z_RAND) + RADIUS_Z;
        int chests = random.nextInt(CHESTS) + CHESTS_RAND;
        int openingCounter = 0; // Counts the amount of 'holes' that would be available when the dungeon should be placed

        // Check if this is a valid spot for a dungeon
        for (int xr = x - radiusX - 1; xr <= x + radiusX + 1; ++xr) {
            for (int yr = y - 1; yr <= y + height + 1; ++yr) {
                for (int zr = z - radiusZ - 1; zr <= z + radiusZ + 1; ++zr) {
                	
                	// Skip invalid chunk generation positions.
                	if(!world.getChunkProvider().chunkExists(xr / 16, yr / 16)) {
                		return false;
                	}
                	
                    Material material = world.getBlock(xr, yr, zr).getMaterial();
                    if (yr == y - 1 && !material.isSolid())
                        return false;
                    if (yr == y + height + 1 && !material.isSolid())
                        return false;
                    if ((xr == x - radiusX - 1 || xr == x + radiusX + 1 || zr == z - radiusZ - 1 || zr == z + radiusZ + 1)
                            && yr == y && world.isAirBlock(xr, yr, zr) && world.isAirBlock(xr, yr + 1, zr))
                        ++openingCounter;
                }
            }
        }

        if (openingCounter >= 1 && openingCounter <= 15) {
            for (int xr = x - radiusX - 1; xr <= x + radiusX + 1; ++xr) {
                for (int yr = y + height; yr >= y - 1; --yr) {
                    for (int zr = z - radiusZ - 1; zr <= z + radiusZ + 1; ++zr) {
                        if (xr != x - radiusX - 1
                                && yr != y - 1
                                && zr != z - radiusZ - 1
                                && xr != x + radiusX + 1
                                && yr != y + height + 1
                                && zr != z + radiusZ + 1) {
                            world.setBlockToAir(xr, yr, zr);
                        } else if (yr >= 0 && !world.getBlock(xr, yr - 1, zr).getMaterial().isSolid()) {
                            world.setBlockToAir(xr, yr, zr);
                        } else if (world.getBlock(xr, yr, zr).getMaterial().isSolid()) {
                            if (yr == y - 1 && random.nextInt(4) != 0) {
								if(Configs.isEnabled(BloodyCobblestoneConfig.class)) {
									world.setBlock(xr, yr, zr, BloodyCobblestoneConfig._instance.getBlockInstance(), 0, 2);
								}
                            } else {
                                world.setBlock(xr, yr, zr, Blocks.cobblestone, 0, 2);
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

                if (world.isAirBlock(xrr, y, zrr)) {
                    int wallCounter = 0;

                    if (world.getBlock(xrr - 1, y, zrr).getMaterial().isSolid())
                        ++wallCounter;

                    if (world.getBlock(xrr + 1, y, zrr).getMaterial().isSolid())
                        ++wallCounter;

                    if (world.getBlock(xrr, y, zrr - 1).getMaterial().isSolid())
                        ++wallCounter;

                    if (world.getBlock(xrr, y, zrr + 1).getMaterial().isSolid())
                        ++wallCounter;

                    if (wallCounter == 1) {
                        world.setBlock(xrr, y, zrr, Blocks.chest, 0, 2);
                        TileEntityChest tileentitychest = (TileEntityChest)world.getTileEntity(xrr, y, zrr);

                        if (tileentitychest != null) {
                            ChestGenHooks info = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
                            WeightedRandomChestContent.generateChestContents(random, info.getItems(random), tileentitychest, info.getCount(random) * 2);
                        }

                        chests--;
                    }
                }
                ++xr;
            }

            for(int xs = x - 1; xs <= x + 1; xs += 2) {
                for(int zs = z - 1; zs <= z + 1; zs += 2) {
                    world.setBlock(xs, y, zs, Blocks.mob_spawner, 0, 2);
                    TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)world.getTileEntity(xs, y, zs);
        
                    if (tileentitymobspawner != null) {
                    	//getSpawnerLogic
                        tileentitymobspawner.func_145881_a().setEntityName(this.pickMobSpawner(random));
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
