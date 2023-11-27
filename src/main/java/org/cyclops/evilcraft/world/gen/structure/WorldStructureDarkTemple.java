package org.cyclops.evilcraft.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Optional;

/**
 * Structure that generates Dark Temples.
 * @author immortaleeb
 * @author rubensworks
 */
public class WorldStructureDarkTemple extends Structure {
    public static final Codec<WorldStructureDarkTemple> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            settingsCodec(builder),
            Codec.INT.fieldOf("minHeight").forGetter(instance -> instance.minHeight),
            Codec.INT.fieldOf("maxHeight").forGetter(instance -> instance.maxHeight)
    ).apply(builder, WorldStructureDarkTemple::new));

    private final int minHeight;
    private final int maxHeight;

    public WorldStructureDarkTemple(Structure.StructureSettings structureSettings, int minHeight, int maxHeight) {
        super(structureSettings);
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (builder) -> generatePieces(builder, context));
    }

    private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
         int y = 9 + Mth.clamp(
                 context.chunkGenerator().getFirstFreeHeight(context.chunkPos().getMiddleBlockX(), context.chunkPos().getMiddleBlockZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState()),
                 this.minHeight,
                 this.maxHeight
         );
        builder.addPiece(new Piece(context.random(),
                context.chunkPos().getMinBlockX(), y, context.chunkPos().getMinBlockZ()));
    }

    @Override
    public StructureType<?> type() {
        return RegistryEntries.STRUCTURE_DARK_TEMPLE;
    }

    public static class Piece extends WorldStructurePieceQuarterSymmetrical {
        public Piece(RandomSource random, int minX, int minY, int minZ) {
            super(WorldStructureDarkTempleConfig.PIECE_TYPE, minX, minY, minZ, 9, 9, 9, getRandomHorizontalDirection(random));
        }

        public Piece(CompoundTag tag) {
            super(WorldStructureDarkTempleConfig.PIECE_TYPE, tag);
        }

        @Override
        protected int getQuarterWidth() {
            return 6;
        }

        @Override
        protected int getQuarterHeight() {
            return 6;
        }

        @Override
        protected void generateLayers() {
            BlockWrapper us = new BlockWrapper(Blocks.STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP));    // upside down stone slab
            BlockWrapper rs = new BlockWrapper(Blocks.STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM));
            BlockWrapper ds = new BlockWrapper(Blocks.STONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE));
            BlockWrapper cb = new BlockWrapper(Blocks.CHISELED_STONE_BRICKS);    // chiseled brick
            BlockWrapper sb = new BlockWrapper(Blocks.STONE_BRICKS);
            BlockWrapper cs = new BlockWrapper(Blocks.COBBLESTONE_SLAB);    // cobblestone slab
            BlockWrapper co = new BlockWrapper(Blocks.COBBLESTONE);
            BlockWrapper wa = new BlockWrapper(Blocks.WATER);
            BlockWrapper fe = new BlockWrapper(Blocks.DARK_OAK_FENCE);
            BlockWrapper to = new BlockWrapper(Blocks.TORCH);
            BlockWrapper cw = new BlockWrapper(Blocks.COBBLESTONE_WALL);
            BlockWrapper lc = new BlockWrapper(Blocks.CHEST.defaultBlockState(), (float) GeneralConfig.darkTempleChestChance);
            lc.action = (world, pos) -> {
                RandomSource rand = RandomSource.create();
                // Static method used instead of manual tile fetch -> member setLootTable to provide compatibility with Lootr.
                RandomizableContainerBlockEntity.setLootTable(world, rand, pos, BuiltInLootTables.JUNGLE_TEMPLE);
            };
            BlockWrapper vi = new BlockWrapper(Blocks.VINE.defaultBlockState(), 0.3F);
            vi.action = (world, pos) -> {
                boolean atLeastOne = false;
                for(Direction side : Direction.Plane.HORIZONTAL) {
                    if(world.getBlockState(pos.relative(side)).isFaceSturdy(world, pos.relative(side), side.getOpposite())) {
                        world.setBlock(pos, Blocks.VINE.defaultBlockState().setValue(
                                VineBlock.PROPERTY_BY_DIRECTION.get(side), true), 2);
                        atLeastOne = true;
                    }
                }
                if(!atLeastOne) {
                    world.removeBlock(pos, true);
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
        protected void postBuildCorner(LevelAccessor world, BlockPos blockPos, int incX, int incZ) {
            // Place upside down stairs in corners

            // x+ east
            // z+ south
            // x- west
            // z- west

            world.setBlock(blockPos.offset(3 * incX, 5, 4 * incZ), Blocks.STONE_STAIRS.defaultBlockState()
                    .setValue(StairBlock.FACING, DirectionHelpers.getEnumFacingFromXSign(incX))
                    .setValue(StairBlock.HALF, Half.TOP)
                    .setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            world.setBlock(blockPos.offset(4 * incX, 5, 3 * incZ), Blocks.STONE_STAIRS.defaultBlockState()
                    .setValue(StairBlock.FACING, DirectionHelpers.getEnumFacingFromZSing(incZ))
                    .setValue(StairBlock.HALF, Half.TOP)
                    .setValue(StairBlock.SHAPE, StairsShape.STRAIGHT), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);

            // pillars to the ground
            int xx = 4 * incX;
            int zz = 4 * incZ;
            int pillarHeight = getPillarHeightForCornerAt(world, blockPos, incX, incZ);

            for (int yOffset = 0; yOffset < pillarHeight; yOffset++) {
                world.setBlock(blockPos.offset(xx, -yOffset, zz), Blocks.COBBLESTONE.defaultBlockState(), MinecraftHelpers.BLOCK_NOTIFY_CLIENT);
            }
        }

        /**
         * @param world The world.
         * @param blockPos The center position of the structure, with the Y-coordinate being the base position.
         * @param incX Indicates the corner X-direction.
         * @param incZ Indicates the cordern Z-direction.
         * @return Returns the height of a pillar in one of the corners, specified by (incX, incZ),
         *         of the temple if it were centered at the given (x,y,z) location.
         */
        private int getPillarHeightForCornerAt(LevelAccessor world, BlockPos blockPos, int incX, int incZ) {
            BlockPos loopPos = blockPos.offset(4 * incX, 0, 4 * incZ);
            int res = 0;
            while (!isSolidBlock(world, loopPos)) {
                loopPos = loopPos.offset(0, -1, 0);
                if (loopPos.getY() <= world.getMinBuildHeight()) {
                    return res;
                }
                res++;
            }
            return res;
        }

        private boolean isSolidBlock(LevelAccessor world, BlockPos blockPos) {
            return isSolidBlock(world.getBlockState(blockPos));
        }

        private boolean isSolidBlock(BlockState blockState) {
            return blockState.isSolid();
        }
    }

}
