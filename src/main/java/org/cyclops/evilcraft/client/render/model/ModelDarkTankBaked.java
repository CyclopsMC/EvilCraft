package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerItemCapacity;
import org.cyclops.cyclopscore.client.model.DelegatingChildDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.block.BlockDarkTank;
import org.cyclops.evilcraft.tileentity.TileDarkTank;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

/**
 * The dynamic item model for the dark tank.
 * @author rubensworks
 */
public class ModelDarkTankBaked extends DelegatingChildDynamicItemAndBlockModel {

    private final int capacity;
    private final FluidStack fluidStack;

    public ModelDarkTankBaked(IBakedModel baseModel) {
        super(baseModel);
        this.capacity = 0;
        this.fluidStack = null;
    }

    public ModelDarkTankBaked(IBakedModel baseModel, int capacity, FluidStack fluidStack,
                              BlockState blockState, Direction facing, Random rand, IModelData modelData) {
        super(baseModel, blockState, facing, rand, modelData);
        this.capacity = capacity;
        this.fluidStack = fluidStack;
    }

    public ModelDarkTankBaked(IBakedModel baseModel, int capacity, FluidStack fluidStack,
                              ItemStack itemStack, World world, LivingEntity entity) {
        super(baseModel, itemStack, world, entity);
        this.capacity = capacity;
        this.fluidStack = fluidStack;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getGeneralQuads() {
        List<BakedQuad> combinedList = Lists.newArrayList();
        if(fluidStack != null) {
            combinedList.addAll(getFluidQuads(fluidStack, capacity));
        }
        combinedList.addAll(baseModel.getQuads(blockState, getRenderingSide(), rand));
        return combinedList;
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull ILightReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        return TileHelpers.getSafeTile(world, pos, TileDarkTank.class)
                .map(tile -> {
                    ModelDataMap.Builder builder = new ModelDataMap.Builder();
                    builder.withInitial(BlockDarkTank.TANK_FLUID, tile.getTank().getFluid());
                    builder.withInitial(BlockDarkTank.TANK_CAPACITY, tile.getTank().getCapacity());
                    return (IModelData) builder.build();
                })
                .orElse(EmptyModelData.INSTANCE);
    }

    @Override
    public IBakedModel handleBlockState(BlockState state, Direction side, Random rand, IModelData modelData) {
        int capacity = ModelHelpers.getSafeProperty(modelData, BlockDarkTank.TANK_CAPACITY, 0);
        FluidStack fluidStack = ModelHelpers.getSafeProperty(modelData, BlockDarkTank.TANK_FLUID, FluidStack.EMPTY);
        return new ModelDarkTankBaked(baseModel, capacity, fluidStack, state, side, rand, modelData);
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack, World world, LivingEntity entity) {
        IFluidHandlerItemCapacity fluidHandler = FluidHelpers.getFluidHandlerItemCapacity(itemStack).orElse(null);
        if(!itemStack.isEmpty() && fluidHandler != null) {
            int capacity = fluidHandler.getCapacity();
            FluidStack fluidStack = FluidHelpers.getFluid(fluidHandler);
            return new ModelDarkTankBaked(baseModel, capacity, fluidStack, itemStack, world, entity);
        }
        return new ModelDarkTankBaked(baseModel, 0, null, itemStack, world, entity);
    }

    protected List<BakedQuad> getFluidQuads(FluidStack fluidStack, int capacity) {
        float height = Math.min(0.99F, ((float) fluidStack.getAmount() / (float) capacity)) / 1.01F;
        List<BakedQuad> quads = Lists.newArrayList();
        for(Direction side : Direction.values()) {
            TextureAtlasSprite texture = org.cyclops.cyclopscore.helper.RenderHelpers.getFluidIcon(fluidStack, side);
            int color = RenderHelpers.getFluidBakedQuadColor(fluidStack);
            if(side == Direction.UP) {
                addBakedQuadRotated(quads, 0.13F, 0.87F, 0.13F, 0.87F, height, texture, side, ROTATION_FIX[side.ordinal()], true, color, ROTATION_UV);
            } else if(side == Direction.DOWN) {
                addBakedQuadRotated(quads, 0.13F, 0.87F, 0.13F, 0.87F, 0.95F, texture, side, ROTATION_FIX[side.ordinal()], true, color, ROTATION_UV);
            } else {
                float x1 = 0.13F;
                float x2 = 0.87F;
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
                float[][] uvs;
                if(side == Direction.UP || side == Direction.DOWN) {
                    uvs = new float[][]{{1, 0}, {1, 1}, {0, 1}, {0, 0}};
                } else {
                    uvs = new float[][]{{1, 0}, {1, height}, {0, height}, {0, 0}};
                }
                addBakedQuadRotated(quads, x1, x2, z1, z2, 0.87F, texture, side, ROTATION_FIX[side.ordinal()], true, color, uvs);
            }
        }
        return quads;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return baseModel.getParticleTexture();
    }

    @Override
    public boolean func_230044_c_() {
        return false; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }
}
