package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.client.model.DelegatingChildDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockDarkTankConfig;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * The dynamic item model for the dark tank.
 * @author rubensworks
 */
public class ModelDarkTankBaked extends DelegatingChildDynamicItemAndBlockModel {

    private final int capacity;
    private final FluidStack fluidStack;

    public ModelDarkTankBaked(BakedModel baseModel) {
        super(baseModel);
        this.capacity = 0;
        this.fluidStack = null;
    }

    public ModelDarkTankBaked(BakedModel baseModel, int capacity, FluidStack fluidStack,
                              BlockState blockState, Direction facing, RandomSource rand, ModelData modelData, RenderType renderType) {
        super(baseModel, blockState, facing, rand, modelData, renderType);
        this.capacity = capacity;
        this.fluidStack = fluidStack;
    }

    public ModelDarkTankBaked(BakedModel baseModel, int capacity, FluidStack fluidStack,
                              ItemStack itemStack, Level world, LivingEntity entity) {
        super(baseModel, itemStack, world, entity);
        this.capacity = capacity;
        this.fluidStack = fluidStack;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getGeneralQuads() {
        List<BakedQuad> combinedList = Lists.newArrayList();
        if(fluidStack != null && !fluidStack.isEmpty() && (BlockDarkTankConfig.staticBlockRendering || isItemStack())) {
            boolean flowing = isItemStack() && RegistryEntries.BLOCK_DARK_TANK.isActivated(itemStack, world);
            combinedList.addAll(getFluidQuads(fluidStack, capacity, flowing));
        }
        combinedList.addAll(baseModel.getQuads(blockState, getRenderingSide(), rand));
        return combinedList;
    }

    @Nonnull
    @Override
    public ModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData tileData) {
        return BlockEntityHelpers.get(world, pos, BlockEntityDarkTank.class)
                .map(tile -> {
                    ModelData.Builder builder = ModelData.builder();
                    builder.with(org.cyclops.evilcraft.block.BlockDarkTank.TANK_FLUID, tile.getTank().getFluid());
                    builder.with(org.cyclops.evilcraft.block.BlockDarkTank.TANK_CAPACITY, tile.getTank().getCapacity());
                    return builder.build();
                })
                .orElse(ModelData.EMPTY);
    }

    @Override
    public BakedModel handleBlockState(BlockState state, Direction side, RandomSource rand, ModelData modelData, RenderType renderType) {
        int capacity = ModelHelpers.getSafeProperty(modelData, org.cyclops.evilcraft.block.BlockDarkTank.TANK_CAPACITY, 0);
        FluidStack fluidStack = ModelHelpers.getSafeProperty(modelData, org.cyclops.evilcraft.block.BlockDarkTank.TANK_FLUID, FluidStack.EMPTY);
        return new ModelDarkTankBaked(baseModel, capacity, fluidStack, state, side, rand, modelData, renderType);
    }

    @Override
    public BakedModel handleItemState(ItemStack itemStack, Level world, LivingEntity entity) {
        IFluidHandlerItemCapacity fluidHandler = FluidHelpers.getFluidHandlerItemCapacity(itemStack).orElse(null);
        if(!itemStack.isEmpty() && fluidHandler != null) {
            int capacity = fluidHandler.getCapacity();
            FluidStack fluidStack = FluidHelpers.getFluid(fluidHandler);
            return new ModelDarkTankBaked(baseModel, capacity, fluidStack, itemStack, world, entity);
        }
        return new ModelDarkTankBaked(baseModel, 0, null, itemStack, world, entity);
    }

    public static TextureAtlasSprite getFluidIcon(FluidStack fluid, boolean flowing, Direction side) {
        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid.getFluid());
        return RenderHelpers.TEXTURE_GETTER.apply(flowing && side != Direction.UP && side != Direction.DOWN
                ? renderProperties.getFlowingTexture(fluid)
                : renderProperties.getStillTexture(fluid));
    }

    protected List<BakedQuad> getFluidQuads(FluidStack fluidStack, int capacity, boolean flowing) {
        float height = Math.min(0.99F, ((float) fluidStack.getAmount() / (float) capacity)) / 1.01F;
        List<BakedQuad> quads = Lists.newArrayList();
        for(Direction side : Direction.values()) {
            TextureAtlasSprite texture = getFluidIcon(fluidStack, flowing, side);
            int color = RenderHelpers.getFluidBakedQuadColor(fluidStack);
            if(side == Direction.UP) {
                addBakedQuadRotated(quads, 0.13F, 0.87F, 0.13F, 0.87F, height, texture, side, ROTATION_FIX[side.ordinal()], true, color, ROTATION_UV);
            } else if(side == Direction.DOWN) {
                addBakedQuadRotated(quads, 0.13F, 0.87F, 0.13F, 0.87F, 0.95F, texture, side, ROTATION_FIX[side.ordinal()], true, color, ROTATION_UV);
            } else {
                float width = 0.87F;
                float x1 = 0.13F;
                float x2 = width;
                float z1 = 0.01F;
                float z2 = height;
                if(side == Direction.EAST || side == Direction.SOUTH) {
                    z1 = 0.99F - height;
                    z2 = 0.99F;
                }
                if(side == Direction.EAST || side == Direction.WEST) {
                    float tmp1 = x1;
                    float tmp2 = x2;
                    x1 = z1;
                    x2 = z2;
                    z1 = tmp1;
                    z2 = tmp2;
                }

                switch (side) {
                    case DOWN:
                        addBakedQuadRotated(quads, x1, x2, z1, z2, width, texture, side, 0  , true, color,
                                new float[][]{{1, 0}, {1, 1}, {0, 1}, {0, 0}});
                        break;
                    case UP:
                        addBakedQuadRotated(quads, x1, x2, z1, z2, width, texture, side, 0, true, color,
                                new float[][]{{1, 0}, {1, 1}, {0, 1}, {0, 0}});
                        break;
                    case NORTH:
                        addBakedQuadRotated(quads, x2, x1, z2, z1, width, texture, side, 0, true, color,
                                new float[][]{{1, 0}, {1, height}, {0, height}, {0, 0}});
                        break;
                    case SOUTH:
                        addBakedQuadRotated(quads, x1, x2, z1, z2, width, texture, side, 0, true, color,
                                new float[][]{{1, 0}, {1, height}, {0, height}, {0, 0}});
                        break;
                    case WEST:
                        // TODO: shadow not right yet
                        addBakedQuadRotated(quads, x1, x2, z1, z2, 0.87F, texture, side, 0, true, color,
                                new float[][]{{1, height}, {0, height}, {0, 0}, {1, 0}});
                        break;
                    case EAST:
                        // TODO: shadow not right yet
                        addBakedQuadRotated(quads, x1, x2, z1, z2, width, texture, side, 0, true, color,
                                new float[][]{{0, 0}, {1, 0}, {1, height}, {0, height}});
                        break;
                }
            }
        }
        return quads;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return baseModel.getParticleIcon();
    }

    @Override
    public boolean usesBlockLight() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public ItemTransforms getTransforms() {
        return ModelHelpers.DEFAULT_CAMERA_TRANSFORMS;
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.of(RenderType.translucent());
    }
}
