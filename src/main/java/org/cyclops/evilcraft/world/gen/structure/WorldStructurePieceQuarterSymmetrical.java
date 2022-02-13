package org.cyclops.evilcraft.world.gen.structure;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.List;
import java.util.Random;

/**
 * @author rubensworks
 */
public abstract class WorldStructurePieceQuarterSymmetrical extends ScatteredStructurePiece {

    private final List<Integer> layerHeights = Lists.newArrayList();
    private final List<BlockWrapper[]> layers = Lists.newArrayList();

    protected WorldStructurePieceQuarterSymmetrical(IStructurePieceType structurePieceTypeIn, Random rand, int xIn, int yIn, int zIn, int widthIn, int heightIn, int depthIn) {
        super(structurePieceTypeIn, rand, xIn, yIn, zIn, widthIn, heightIn, depthIn);
        generateLayers();
    }

    protected WorldStructurePieceQuarterSymmetrical(IStructurePieceType structurePieceTypeIn, CompoundNBT nbt) {
        super(structurePieceTypeIn, nbt);
        generateLayers();
    }

    protected abstract int getQuarterWidth();

    protected abstract int getQuarterHeight();

    protected abstract void generateLayers();

    protected void addLayer(int height, BlockWrapper[] layer) {
        layerHeights.add(height);
        layers.add(layer);
    }

    protected void buildCorner(IWorld world, BlockPos blockPos, int incX, int incZ) {
        Random r = new Random();
        for (int i = 0; i < layerHeights.size(); ++i) {
            int layerHeight = layerHeights.get(i);
            BlockWrapper[] layer = layers.get(i);

            // Don't overwrite the borders everytime we place blocks
            int start = (incX == incZ) ? 0 : 1;

            for (int zr = start; zr < getQuarterHeight(); ++zr) {
                for (int xr = start; xr < getQuarterWidth(); ++xr) {
                    BlockWrapper wrapper = layer[(getQuarterWidth() - xr - 1) * getQuarterHeight() + zr];
                    BlockPos posOffset = blockPos.offset(xr * incX, layerHeight, zr * incZ);
                    if (wrapper != null && (wrapper.chance > 0 && wrapper.chance >= r.nextFloat())) { // not an air blockState?
                        world.setBlock(blockPos.offset(xr * incX, layerHeight, zr * incZ), wrapper.blockState, MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
                        if(wrapper.action != null) {
                            wrapper.action.run(world, posOffset);
                        }
                    }

                }
            }
        }

        postBuildCorner(world, blockPos, incX, incZ);
    }

    protected void postBuildCorner(IWorld world, BlockPos pos, int incX, int incZ) {
        for (int i = 0; i < layerHeights.size(); ++i) {
            int layerHeight = layerHeights.get(i);
            BlockWrapper[] layer = layers.get(i);

            // Don't overwrite the borders everytime we place blocks
            int start = (incX == incZ) ? 0 : 1;

            for (int zr = start; zr < getQuarterHeight(); ++zr) {
                for (int xr = start; xr < getQuarterWidth(); ++xr) {
                    BlockWrapper wrapper = layer[(getQuarterWidth() - xr - 1) * getQuarterHeight() + zr];
                    if (wrapper != null) {
                        BlockPos posOffset = pos.offset(xr * incX, layerHeight, zr * incZ);
                        if(wrapper.action != null && world.getBlockState(posOffset) == wrapper.blockState) {
                            wrapper.action.run(world, posOffset);
                        }
                    }
                }
            }
        }
    }

    // init
    @Override
    public boolean postProcess(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator,
                                  Random rand, MutableBoundingBox bounds, ChunkPos chunkPos, BlockPos pos) {
        int x = rand.nextInt(16);
        int z = rand.nextInt(16);
        BlockPos blockPos = new BlockPos(this.getWorldX(x, z), this.getWorldY(0), this.getWorldZ(x, z));
        buildCorner(world, blockPos, 1, 1);
        buildCorner(world, blockPos, -1, 1);
        buildCorner(world, blockPos, 1, -1);
        buildCorner(world, blockPos, -1, -1);
        return true;
    }

    /**
     * This is a wrapper class, which wraps around a {@link Block} and
     * pairs with it the metadata for that specific blockState instance.
     *
     * @author immortaleeb
     *
     */
    public class BlockWrapper {
        /**
         * {@link BlockState} for which this instance is a wrapper.
         */
        public BlockState blockState;
        /**
         * Chance of spawning
         */
        public float chance = 1;
        /**
         * An optional action after a block has been placed.
         */
        public IBlockAction action = null;

        /**
         * Creates a new wrapper around the specified {@link Block} with metadata 0.
         * @param block The blockState to wrap.
         */
        public BlockWrapper(Block block) {
            this(block.defaultBlockState());
        }

        /**
         * Creates a new wrapper around the specified {@link BlockState} with the specified metadata.
         * @param blockState The blockState to wrap.
         */
        public BlockWrapper(BlockState blockState) {
            this.blockState = blockState;
        }

        /**
         * Creates a new wrapper around the specified {@link Block} with the specified metadata and a certain chance.
         * @param blockState The block to wrap.
         * @param chance The chance on spawning.
         */
        public BlockWrapper(BlockState blockState, float chance) {
            this(blockState);
            this.chance = chance;
        }
    }

    public static interface IBlockAction {
        public void run(IWorld world, BlockPos pos);
    }
}
