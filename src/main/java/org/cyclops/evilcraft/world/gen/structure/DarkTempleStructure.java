package org.cyclops.evilcraft.world.gen.structure;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.WorldHelpers;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Structure which generates Dark Temples.
 *
 * @author immortaleeb
 * @author rubensworks
 */
public class DarkTempleStructure extends QuarterSymmetricalStructure {

    private static final int STRUCTURE_HEIGHT = 9;

    private static final int[] CORNER_INC = {-1, 1};

    private static DarkTempleStructure _instance = null;

    /**
     * Get the unique instance.
     *
     * @return Unique instance.
     */
    public static DarkTempleStructure getInstance() {
        if (_instance == null)
            _instance = new DarkTempleStructure();

        return _instance;
    }

    private DarkTempleStructure() {
        super(6, 6);
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
    private int findGround(World world, int x, int z, int yMin, int yMax) {
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

    private boolean canPlaceStructure(World world, BlockPos blockPos) {
        return WorldHelpers.foldArea(world, new int[]{3, 0, 3}, new int[]{3, 9, 3}, blockPos, new WorldHelpers.WorldFoldingFunction<Boolean, Boolean>() {
            @Nullable
            @Override
            public Boolean apply(@Nullable Boolean from, World world, BlockPos blockPos) {
                return from && !isSolidBlock(world, blockPos);
            }
        }, true);
    }

    private boolean isValidSpot(World world, BlockPos blockPos) {
        IBlockState blockState = world.getBlockState(blockPos);
        return isSolidBlock(blockState) || blockState.getBlock().isReplaceable(world, blockPos);
    }

    private boolean isSolidBlock(World world, BlockPos blockPos) {
        return isSolidBlock(world.getBlockState(blockPos));
    }

    private boolean isSolidBlock(IBlockState blockState) {
        Material material = blockState.getMaterial();
        return material.isSolid() && material.isOpaque();
    }

    private int getMaxPillarHeightAt(World world, BlockPos blockPos) {
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
    private int getPillarHeightForCornerAt(World world, BlockPos blockPos, int incX, int incZ) {
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
        BlockWrapper us = new BlockWrapper(Blocks.stone_slab.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP));    // upside down stone slab
        BlockWrapper rs = new BlockWrapper(Blocks.stone_slab);
        BlockWrapper ds = new BlockWrapper(Blocks.double_stone_slab);
        BlockWrapper cb = new BlockWrapper(Blocks.stonebrick.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED));    // chiseled brick
        BlockWrapper sb = new BlockWrapper(Blocks.stonebrick);
        BlockWrapper cs = new BlockWrapper(Blocks.stone_slab.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE));    // cobblestone slab
        BlockWrapper co = new BlockWrapper(Blocks.cobblestone);
        BlockWrapper wa = new BlockWrapper(Blocks.water);
        BlockWrapper fe = new BlockWrapper(Blocks.dark_oak_fence);
        BlockWrapper to = new BlockWrapper(Blocks.torch);
        BlockWrapper cw = new BlockWrapper(Blocks.cobblestone_wall);
        BlockWrapper lc = new BlockWrapper(Blocks.chest.getDefaultState(), 0.15F);
        lc.action = new IBlockAction() {
            @Override
            public void run(World world, BlockPos pos) {
                Random rand = new Random();
                TileEntityChest tile = (TileEntityChest) world.getTileEntity(pos);
                if (tile != null) {
                    tile.setLoot(LootTableList.CHESTS_SPAWN_BONUS_CHEST, rand.nextLong());
                }
            }
        };
        BlockWrapper vi = new BlockWrapper(Blocks.vine.getDefaultState(), 0.3F);
        vi.action = new IBlockAction() {
            @Override
            public void run(World world, BlockPos pos) {
                boolean atLeastOne = false;
                for(EnumFacing side : EnumFacing.HORIZONTALS) {
                    if(world.isSideSolid(pos.offset(side), side.getOpposite(), true)) {
                        world.setBlockState(pos, Blocks.vine.getDefaultState().withProperty(
                                BlockVine.ALL_FACES[side.ordinal() - 1], true), 2);
                        atLeastOne = true;
                    }
                }
                if(!atLeastOne) {
                    world.setBlockToAir(pos);
                }
            }
        };

        BlockWrapper ea = new BlockWrapper(EnvironmentalAccumulator.getInstance());
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
    protected void postBuildCorner(World world, BlockPos blockPos, int incX, int incZ) {
        // Place upside down stairs in corners

        // x+ east
        // z+ south
        // x- west
        // z- west

        world.setBlockState(blockPos.add(3 * incX, 5, 4 * incZ), Blocks.stone_stairs.getDefaultState()
                .withProperty(BlockStairs.FACING, DirectionHelpers.getEnumFacingFromXSign(incX))
                .withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP)
                .withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        world.setBlockState(blockPos.add(4 * incX, 5, 3 * incZ), Blocks.stone_stairs.getDefaultState()
                .withProperty(BlockStairs.FACING, DirectionHelpers.getEnumFacingFromZSing(incZ))
                .withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP)
                .withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.STRAIGHT), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);

        // pillars to the ground
        int xx = 4 * incX;
        int zz = 4 * incZ;
        int pillarHeight = getPillarHeightForCornerAt(world, blockPos, incX, incZ);

        for (int yOffset = 0; yOffset < pillarHeight; yOffset++) {
            world.setBlockState(blockPos.add(xx, -yOffset, zz), Blocks.cobblestone.getDefaultState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
        }
    }

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
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
                super.generate(world, random, location);

                return true;
            }

            groundHeight = findGround(world, x, z, getMinBuildHeight(), groundHeight-1);
        }

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
