package org.cyclops.evilcraft.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.storage.loot.LootTables;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.world.gen.structure.WorldFeatureQuarterSymmetrical;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Function;

/**
 * Structure which generates Dark Temples.
 *
 * @author immortaleeb
 * @author rubensworks
 */
public class WorldFeatureDarkTemple extends WorldFeatureQuarterSymmetrical {

    private static final int STRUCTURE_HEIGHT = 9;

    private static final int[] CORNER_INC = {-1, 1};

    public WorldFeatureDarkTemple(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn, 6, 6);
    }

    /**
     * Finds the y coordinate of a block that is at ground-level
     * given its x and z coordinates, between yMin and yMax
     * (borders included) and that is a valid block to place a
     * dark temple on.
     *
     * @param world The world.
     * @param x     x-coordinate.
     * @param z     y-coordinate.
     * @return The y-coordinate of a block on the ground, or -1 if no y was found
     * in the range yMin to yMax (borders included).
     */
    private int findGround(IWorld world, int x, int z, int yMin, int yMax) {
        if (yMin <= yMax) {
            int height = yMax;

            // Try all possible y-coordinates from yMax to yMin
            while (height >= yMin) {
                // Look for the first block that is not an air block and has an air block above it
                while (height >= yMin && (world.isAirBlock(new BlockPos(x, height, z)) || !world.isAirBlock(new BlockPos(x, height + 1, z)))) {
                    height--;
                }

                // Is it a valid spot to place the center of a dark temple on?
                if (height >= yMin && isValidSpot(world, new BlockPos(x, height, z)))
                    return height;

                height--;
            }
        }

        return -1;
    }

    private boolean canPlaceStructure(IWorld world, BlockPos blockPos) {
        return WorldHelpers.foldArea((World) world, new int[]{3, 0, 3}, new int[]{3, 9, 3}, blockPos, new WorldHelpers.WorldFoldingFunction<Boolean, Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable Boolean from, World world, BlockPos blockPos) {
                return from && !isSolidBlock(world, blockPos);
            }
        }, true);
    }

    private boolean isValidSpot(IWorld world, BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);
        return isSolidBlock(blockState) || blockState.canBeReplacedByLogs(world, blockPos);
    }

    private boolean isSolidBlock(IWorld world, BlockPos blockPos) {
        return isSolidBlock(world.getBlockState(blockPos));
    }

    private boolean isSolidBlock(BlockState blockState) {
        Material material = blockState.getMaterial();
        return material.isSolid() && material.isOpaque();
    }

    private int getMaxPillarHeightAt(IWorld world, BlockPos blockPos) {
        int max = 0;

        for (Integer incX : CORNER_INC) {
            for (Integer incZ : CORNER_INC) {
                max = Math.max(max, getPillarHeightForCornerAt(world, blockPos, incX, incZ));
            }
        }

        return max;
    }

    /**
     * @param world The world.
     * @param blockPos The center position of the structure, with the Y-coordinate being the base position.
     * @param incX Indicates the corner X-direction.
     * @param incZ Indicates the cordern Z-direction.
     * @return Returns the height of a pillar in one of the corners, specified by (incX, incZ),
     *         of the temple if it were centered at the given (x,y,z) location.
     */
    private int getPillarHeightForCornerAt(IWorld world, BlockPos blockPos, int incX, int incZ) {
        BlockPos loopPos = blockPos.add(4 * incX, 0, 4 * incZ);
        int res = 0;

        while (!isSolidBlock(world, loopPos)) {
            loopPos = loopPos.add(0, -1, 0);
            res++;
        }
        return res;
    }

    @Override
    protected void generateLayers() {
        BlockWrapper us = new BlockWrapper(Blocks.STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP));    // upside down stone slab
        BlockWrapper rs = new BlockWrapper(Blocks.STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM));
        BlockWrapper ds = new BlockWrapper(Blocks.STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE));
        BlockWrapper cb = new BlockWrapper(Blocks.CHISELED_STONE_BRICKS);    // chiseled brick
        BlockWrapper sb = new BlockWrapper(Blocks.STONE_BRICKS);
        BlockWrapper cs = new BlockWrapper(Blocks.COBBLESTONE_SLAB);    // cobblestone slab
        BlockWrapper co = new BlockWrapper(Blocks.COBBLESTONE);
        BlockWrapper wa = new BlockWrapper(Blocks.WATER);
        BlockWrapper fe = new BlockWrapper(Blocks.DARK_OAK_FENCE);
        BlockWrapper to = new BlockWrapper(Blocks.TORCH);
        BlockWrapper cw = new BlockWrapper(Blocks.COBBLESTONE_WALL);
        BlockWrapper lc = new BlockWrapper(Blocks.CHEST.getDefaultState(), (float) GeneralConfig.darkTempleChestChance);
        lc.action = new IBlockAction() {
            @Override
            public void run(IWorld world, BlockPos pos) {
                Random rand = new Random();
                ChestTileEntity tile = (ChestTileEntity) world.getTileEntity(pos);
                if (tile != null) {
                    tile.setLootTable(LootTables.CHESTS_JUNGLE_TEMPLE, rand.nextLong());
                }
            }
        };
        BlockWrapper vi = new BlockWrapper(Blocks.VINE.getDefaultState(), 0.3F);
        vi.action = new IBlockAction() {
            @Override
            public void run(IWorld world, BlockPos pos) {
                boolean atLeastOne = false;
                for(Direction side : Direction.Plane.HORIZONTAL) {
                    if(world.getBlockState(pos.offset(side)).isSolidSide(world, pos.offset(side), side.getOpposite())) {
                        world.setBlockState(pos, Blocks.VINE.getDefaultState().with(
                                VineBlock.FACING_TO_PROPERTY_MAP.get(side), true), 2);
                        atLeastOne = true;
                    }
                }
                if(!atLeastOne) {
                    world.removeBlock(pos, true);
                }
            }
        };

        BlockWrapper ea = new BlockWrapper(RegistryEntries.BLOCK_ENVIRONMENTAL_ACCUMULATOR);
        BlockWrapper o = null;    // Just to keep things compact...

        addLayer(1, new BlockWrapper[]{
                o, o, o, o, vi, o,
                o, o, o, us, ds, vi,
                us, us, us, us, us, o,
                us, us, us, us, o, o,
                us, us, us, us, o, o,
                us, us, us, us, o, o
        });

        addLayer(2, new BlockWrapper[]{
                o, o, o, vi, vi, o,
                o, o, o, cb, cb, vi,
                sb, sb, sb, sb, cb, vi,
                ds, co, wa, sb, o, o,
                co, co, co, sb, o, o,
                co, co, ds, sb, o, o
        });

        addLayer(3, new BlockWrapper[]{
                o, o, o, o, vi, o,
                o, o, o, lc, sb, vi,
                o, o, o, fe, lc, o,
                rs, o, o, o, o, o,
                cs, rs, o, o, o, o,
                ea, cs, rs, o, o, o
        });

        addLayer(4, new BlockWrapper[]{
                o, o, o, o, vi, o,
                o, o, o, vi, cb, vi,
                o, o, o, to, vi, o,
                o, o, o, o, o, o,
                o, o, o, o, o, o,
                o, o, o, o, o, o
        });

        addLayer(5, new BlockWrapper[]{
                us, o, o, o, cw, o,
                o, o, o, o, sb, cw,
                o, o, o, o, o, o,
                o, o, o, o, o, o,
                o, o, o, o, o, o,
                o, o, o, o, o, us
        });

        addLayer(6, new BlockWrapper[]{
                cb, ds, rs, rs, rs, o,
                co, co, co, co, co, rs,
                co, co, co, co, co, rs,
                co, co, co, co, co, rs,
                co, co, co, co, co, ds,
                o, co, co, co, co, cb
        });

        addLayer(7, new BlockWrapper[]{
                rs, o, o, o, o, o,
                cw, o, o, o, o, o,
                cs, cs, cs, o, o, o,
                co, co, cs, cs, o, o,
                co, co, co, cs, o, o,
                o, co, co, cs, cw, rs
        });

        addLayer(8, new BlockWrapper[]{
                o, o, o, o, o, o,
                o, o, o, o, o, o,
                o, o, o, o, o, o,
                o, o, o, o, o, o,
                cs, cw, o, o, o, o,
                o, cs, o, o, o, o
        });

        addLayer(9, new BlockWrapper[]{
                o, o, o, o, o, o,
                o, o, o, o, o, o,
                o, o, o, o, o, o,
                o, o, o, o, o, o,
                o, to, o, o, o, o,
                o, o, o, o, o, o
        });
    }

    @Override
    protected void postBuildCorner(IWorld world, BlockPos blockPos, int incX, int incZ) {
        // Place upside down stairs in corners

        // x+ east
        // z+ south
        // x- west
        // z- west

        world.setBlockState(blockPos.add(3 * incX, 5, 4 * incZ), Blocks.STONE_STAIRS.getDefaultState()
                .with(StairsBlock.FACING, DirectionHelpers.getEnumFacingFromXSign(incX))
                .with(StairsBlock.HALF, Half.TOP)
                .with(StairsBlock.SHAPE, StairsShape.STRAIGHT), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        world.setBlockState(blockPos.add(4 * incX, 5, 3 * incZ), Blocks.STONE_STAIRS.getDefaultState()
                .with(StairsBlock.FACING, DirectionHelpers.getEnumFacingFromZSing(incZ))
                .with(StairsBlock.HALF, Half.TOP)
                .with(StairsBlock.SHAPE, StairsShape.STRAIGHT), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);

        // pillars to the ground
        int xx = 4 * incX;
        int zz = 4 * incZ;
        int pillarHeight = getPillarHeightForCornerAt(world, blockPos, incX, incZ);

        for (int yOffset = 0; yOffset < pillarHeight; yOffset++) {
            world.setBlockState(blockPos.add(xx, -yOffset, zz), Blocks.COBBLESTONE.getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        }
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos blockPos, NoFeatureConfig config) {
        int x = blockPos.getX();
        int z = blockPos.getZ();
        int groundHeight = findGround(world, x, z, getMinBuildHeight(), getMaxBuildHeight());

        while (groundHeight != -1) {
            BlockPos location = new BlockPos(x, groundHeight, z);
            // Check if we have room to place the structure here
            if (canPlaceStructure(world, location.add(0, 1, 0))) {
                // Only place the structure if the pillars are not too long
                if (getMaxPillarHeightAt(world, blockPos) > GeneralConfig.darkTempleMaxPillarLength) return false;
                // If all is ok, generate the structure
                super.place(world, generator, rand, blockPos, config);

                return true;
            }

            groundHeight = findGround(world, x, z, getMinBuildHeight(), groundHeight-1);
        }
        EvilCraft.darkTempleData.addFailedLocation(world.getDimension(), blockPos.getX() / WorldHelpers.CHUNK_SIZE, blockPos.getZ() / WorldHelpers.CHUNK_SIZE);

        return false;
    }

    private static int getMinBuildHeight() {
        return GeneralConfig.darkTempleMinHeight;
    }

    private static int getMaxBuildHeight() {
        return GeneralConfig.darkTempleMaxHeight - STRUCTURE_HEIGHT;
    }

    // Good seed for testing: -8353386167196066531

}
