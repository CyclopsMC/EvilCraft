package org.cyclops.evilcraft.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;


/**
 * Evil Dungeon, based on vanilla dungeons
 * @author rubensworks
 *
 */
public class WorldFeatureEvilDungeon extends MonsterRoomFeature {

    private static final int RADIUS_X = 3;
    private static final int RADIUS_X_RAND = 4;
    private static final int RADIUS_Z = 3;
    private static final int RADIUS_Z_RAND = 4;
    private static final int CHESTS = 2;
    private static final int CHESTS_RAND = 2;

    public WorldFeatureEvilDungeon(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel world = context.level();
        BlockPos blockPos = context.origin();
        RandomSource random = context.random();

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
                    if(!world.getChunkSource().hasChunk(xr / 16, yr / 16)) {
                        return false;
                    }

                    BlockState blockState = world.getBlockState(loopPos);
                    if (yr == y - 1 && !blockState.isSolid())
                        return false;
                    if (yr == y + height + 1 && !blockState.isSolid())
                        return false;
                    if ((xr == x - radiusX - 1 || xr == x + radiusX + 1 || zr == z - radiusZ - 1 || zr == z + radiusZ + 1)
                            && yr == y && world.isEmptyBlock(loopPos) && world.isEmptyBlock(loopPos.offset(0, 1, 0)))
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
                        } else if (yr >= 0 && !world.getBlockState(loopPos.offset(0, -1, 0)).isSolid()) {
                            world.removeBlock(loopPos, false);
                        } else if (world.getBlockState(loopPos).isSolid()) {
                            if (yr == y - 1 && random.nextInt(4) != 0) {
                                world.setBlock(loopPos, RegistryEntries.BLOCK_BLOODY_COBBLESTONE.defaultBlockState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                            } else {
                                world.setBlock(loopPos, Blocks.COBBLESTONE.defaultBlockState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
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

                if (world.isEmptyBlock(loopPos)) {
                    int wallCounter = 0;

                    if (world.getBlockState(loopPos.offset(-1, 0, 0)).isSolid())
                        ++wallCounter;

                    if (world.getBlockState(loopPos.offset(1, 0, 0)).isSolid())
                        ++wallCounter;

                    if (world.getBlockState(loopPos.offset(0, 0, -1)).isSolid())
                        ++wallCounter;

                    if (world.getBlockState(loopPos.offset(0, 0, 1)).isSolid())
                        ++wallCounter;

                    if (wallCounter == 1) {
                        world.setBlock(loopPos, Blocks.CHEST.defaultBlockState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                         // Static method used instead of manual tile fetch -> member setLootTable to provide compatibility with Lootr.
                        RandomizableContainerBlockEntity.setLootTable(world, random, loopPos, BuiltInLootTables.SIMPLE_DUNGEON);

                        chests--;
                    }
                }
                ++xr;
            }

            for(int xs = x - 1; xs <= x + 1; xs += 2) {
                for(int zs = z - 1; zs <= z + 1; zs += 2) {
                    BlockPos loopPos = new BlockPos(xs, y, zs);
                    world.setBlock(loopPos, Blocks.SPAWNER.defaultBlockState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                    BlockEntity tile = world.getBlockEntity(loopPos);

                    if (tile instanceof SpawnerBlockEntity) {
                        ((SpawnerBlockEntity) tile).getSpawner().setEntityId(net.minecraftforge.common.DungeonHooks.getRandomDungeonMob(random), null, random, loopPos);
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
