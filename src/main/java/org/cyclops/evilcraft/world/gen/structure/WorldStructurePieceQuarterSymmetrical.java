package org.cyclops.evilcraft.world.gen.structure;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.util.List;
import java.util.Random;

/**
 * @author rubensworks
 */
public abstract class WorldStructurePieceQuarterSymmetrical extends ScatteredFeaturePiece {

    private final List<Integer> layerHeights = Lists.newArrayList();
    private final List<BlockWrapper[]> layers = Lists.newArrayList();

    protected WorldStructurePieceQuarterSymmetrical(StructurePieceType structurePieceTypeIn, int xIn, int yIn, int zIn, int widthIn, int heightIn, int depthIn, Direction direction) {
        super(structurePieceTypeIn, xIn, yIn, zIn, widthIn, heightIn, depthIn, direction);
        generateLayers();
    }

    protected WorldStructurePieceQuarterSymmetrical(StructurePieceType structurePieceTypeIn, CompoundTag nbt) {
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

    protected void buildCorner(LevelAccessor world, BlockPos blockPos, int incX, int incZ) {
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

    protected void postBuildCorner(LevelAccessor world, BlockPos pos, int incX, int incZ) {
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

    @Override
    public void postProcess(WorldGenLevel world, StructureManager structureManager, ChunkGenerator chunkGenerator,
                            RandomSource rand, BoundingBox bounds, ChunkPos chunkPos, BlockPos pos) {
        int x = rand.nextInt(16);
        int z = rand.nextInt(16);
        BlockPos blockPos = new BlockPos(this.getWorldX(x, z), this.getWorldY(0), this.getWorldZ(x, z));
        buildCorner(world, blockPos, 1, 1);
        buildCorner(world, blockPos, -1, 1);
        buildCorner(world, blockPos, 1, -1);
        buildCorner(world, blockPos, -1, -1);
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
        public void run(LevelAccessor world, BlockPos pos);
    }
}
