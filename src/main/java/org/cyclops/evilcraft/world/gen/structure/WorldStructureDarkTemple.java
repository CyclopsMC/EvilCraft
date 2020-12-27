package org.cyclops.evilcraft.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.VineBlock;
import net.minecraft.block.material.Material;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import org.cyclops.cyclopscore.helper.DirectionHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.GeneralConfig;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.Random;

/**
 * Structure that generates Dark Temples.
 * @author immortaleeb
 * @author rubensworks
 */
public class WorldStructureDarkTemple extends Structure<NoFeatureConfig> {

    public WorldStructureDarkTemple(Codec<NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    protected boolean func_230363_a_(ChunkGenerator p_230363_1_, BiomeProvider p_230363_2_, long p_230363_3_, SharedSeedRandom p_230363_5_, int p_230363_6_, int p_230363_7_, Biome biome, ChunkPos p_230363_9_, NoFeatureConfig p_230363_10_) {
        if (biome.getCategory() == Biome.Category.OCEAN) {
            return false;
        }
        return super.func_230363_a_(p_230363_1_, p_230363_2_, p_230363_3_, p_230363_5_, p_230363_6_, p_230363_7_, biome, p_230363_9_, p_230363_10_);
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox bounds, int references, long seed) {
            super(structure, chunkPosX, chunkPosZ, bounds, references, seed);
        }

        // MCP: init
        @Override
        public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator p_230364_2_, TemplateManager p_230364_3_, int chunkX, int chunkY, Biome p_230364_6_, NoFeatureConfig p_230364_7_) {
            Piece piece = new Piece(this.rand, chunkX * 16, chunkY * 16);
            this.components.add(piece);
            this.recalculateStructureSize();
        }
    }

    public static class Piece extends WorldStructurePieceQuarterSymmetrical {
        public Piece(Random random, int x, int z) {
            super(WorldStructureDarkTempleConfig.PIECE_TYPE, random, x, 9 + WorldStructureDarkTempleConfig.darkTempleMinHeight + random.nextInt(WorldStructureDarkTempleConfig.darkTempleMaxHeight - WorldStructureDarkTempleConfig.darkTempleMinHeight), z, 9, 9, 9);
        }

        public Piece(TemplateManager templateManager, CompoundNBT tag) {
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
            lc.action = (world, pos) -> {
                Random rand = new Random();
                ChestTileEntity tile = (ChestTileEntity) world.getTileEntity(pos);
                if (tile != null) {
                    tile.setLootTable(LootTables.CHESTS_JUNGLE_TEMPLE, rand.nextLong());
                }
            };
            BlockWrapper vi = new BlockWrapper(Blocks.VINE.getDefaultState(), 0.3F);
            vi.action = (world, pos) -> {
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

        private boolean isSolidBlock(IWorld world, BlockPos blockPos) {
            return isSolidBlock(world.getBlockState(blockPos));
        }

        private boolean isSolidBlock(BlockState blockState) {
            Material material = blockState.getMaterial();
            return material.isSolid() && material.isOpaque();
        }
    }

}
