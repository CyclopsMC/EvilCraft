package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;
import org.cyclops.cyclopscore.client.model.DelegatingChildDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityDarkTank;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * The dynamic item model for the dark tank.
 * @author rubensworks
 */
public class ModelDarkTankBaked extends DelegatingChildDynamicItemAndBlockModel {

    private final int capacity;
    private final FluidStack fluidStack;

    public ModelDarkTankBaked(BlockStateModel baseModel) {
        super(baseModel);
        this.capacity = 0;
        this.fluidStack = null;
    }

    public ModelDarkTankBaked(BlockStateModel baseModel, int capacity, FluidStack fluidStack,
                              BlockAndTintGetter level, BlockState blockState, Direction facing, RandomSource rand, ModelData modelData, ChunkSectionLayer renderType) {
        super(baseModel, level, blockState, facing, rand, modelData, renderType);
        this.capacity = capacity;
        this.fluidStack = fluidStack;
    }

    public ModelDarkTankBaked(BlockStateModel baseModel, int capacity, FluidStack fluidStack,
                              ItemStack itemStack, ClientLevel world, LivingEntity entity) {
        super(baseModel, itemStack, world, entity);
        this.capacity = capacity;
        this.fluidStack = fluidStack;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getGeneralQuads() {
        List<BakedQuad> combinedList = Lists.newArrayList();
        if(fluidStack != null && !fluidStack.isEmpty() && isItemStack()) {
            boolean flowing = isItemStack() && RegistryEntries.BLOCK_DARK_TANK.get().isActivated(itemStack, Item.TooltipContext.EMPTY);
            combinedList.addAll(getFluidQuads(fluidStack, capacity, flowing));
        }
        List<BlockStateModelPart> baseParts = new ArrayList<>();
        baseModel.collectParts(level, BlockPos.ZERO, blockState, rand, baseParts);
        for (BlockStateModelPart blockModelPart : baseParts) {
            combinedList.addAll(blockModelPart.getQuads(null));
        }
        return combinedList;
    }

    @Nonnull
    @Override
    public ModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull ModelData tileData) {
        return IModHelpers.get().getBlockEntityHelpers().get(world, pos, BlockEntityDarkTank.class)
                .map(tile -> {
                    ModelData.Builder builder = ModelData.builder();
                    builder.with(org.cyclops.evilcraft.block.BlockDarkTank.TANK_FLUID, tile.getTank().getFluid());
                    builder.with(org.cyclops.evilcraft.block.BlockDarkTank.TANK_CAPACITY, tile.getTank().getCapacity());
                    return builder.build();
                })
                .orElse(ModelData.EMPTY);
    }

    @Override
    public List<ChunkSectionLayer> getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        return List.of(ChunkSectionLayer.TRANSLUCENT);
    }

    @Override
    public List<BakedQuad> handleBlockState(BlockAndTintGetter level, BlockPos pos, BlockState state, Direction side, RandomSource rand, ModelData extraData, ChunkSectionLayer renderType) {
        int capacity = ModelHelpers.getSafeProperty(modelData, org.cyclops.evilcraft.block.BlockDarkTank.TANK_CAPACITY, 0);
        FluidStack fluidStack = ModelHelpers.getSafeProperty(modelData, org.cyclops.evilcraft.block.BlockDarkTank.TANK_FLUID, FluidStack.EMPTY);
        return new ModelDarkTankBaked(baseModel, capacity, fluidStack, level, state, side, rand, modelData, renderType).getGeneralQuads();
    }

    @Override
    public List<BakedQuad> handleItemState(ItemStack itemStack, net.minecraft.world.level.Level world, ItemOwner entity) {
        IFluidHandlerCapacity fluidHandler = IModHelpersNeoForge.get().getFluidHelpers().getFluidHandlerItemCapacity(ItemAccess.forStack(itemStack)).orElse(null);
        if(!itemStack.isEmpty() && fluidHandler != null) {
            int capacity = fluidHandler.getTankCapacity(0);
            FluidStack fluidStack = IModHelpersNeoForge.get().getFluidHelpers().getFluid(fluidHandler);
            return new ModelDarkTankBaked(baseModel, capacity, fluidStack, itemStack, (ClientLevel) world, entity != null ? entity.asLivingEntity() : null).getGeneralQuads();
        }
        return new ModelDarkTankBaked(baseModel, 0, null, itemStack, (ClientLevel) world, entity.asLivingEntity()).getGeneralQuads();
    }

    public static TextureAtlasSprite getFluidIcon(FluidStack fluid, boolean flowing, Direction side) {
        FluidModel model = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluid.getFluid().defaultFluidState());
        if (!flowing || side == Direction.UP || side == Direction.DOWN) {
            return model.stillMaterial().sprite();
        }
        return model.flowingMaterial().sprite();
    }

    protected List<BakedQuad> getFluidQuads(FluidStack fluidStack, int capacity, boolean flowing) {
        float height = Math.min(0.99F, ((float) fluidStack.getAmount() / (float) capacity)) / 1.01F;
        List<BakedQuad> quads = Lists.newArrayList();
        for(Direction side : Direction.values()) {
            TextureAtlasSprite texture = getFluidIcon(fluidStack, flowing, side);
            int color = IModHelpersNeoForge.get().getRenderHelpers().getFluidBakedQuadColor(fluidStack);
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
                        addBakedQuadRotated(quads, x1, x2, z1, z2, 0.87F, texture, side, 0, true, color,
                                new float[][]{{1, height}, {0, height}, {0, 0}, {1, 0}});
                        break;
                    case EAST:
                        addBakedQuadRotated(quads, x1, x2, z1, z2, width, texture, side, 0, true, color,
                                new float[][]{{0, 0}, {1, 0}, {1, height}, {0, height}});
                        break;
                }
            }
        }
        return quads;
    }

    @Override
    public Material.Baked particleMaterial() {
        return baseModel.particleMaterial();
    }

    @Override
    public int materialFlags() {
        return 0;
    }

    @Override
    public boolean usesBlockLight() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public UnbakedModel wrapped() {
        return null;
    }

    @Override
    public @Nullable ResolvedModel parent() {
        return null;
    }

    @Override
    public ItemTransforms getTopTransforms() {
        return ModelHelpers.DEFAULT_CAMERA_TRANSFORMS;
    }

    @Override
    public String debugName() {
        return Reference.MOD_ID + ":dark_tank";
    }
}
