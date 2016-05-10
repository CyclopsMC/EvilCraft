package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.client.model.DelegatingChildDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.block.DarkTank;
import org.cyclops.evilcraft.core.block.IBlockTank;

import java.util.List;

/**
 * The dynamic item model for the dark tank.
 * @author rubensworks
 */
public class ModelDarkTank extends DelegatingChildDynamicItemAndBlockModel {

    private final int capacity;
    private final FluidStack fluidStack;

    public ModelDarkTank(IBakedModel baseModel) {
        super(baseModel);
        this.capacity = 0;
        this.fluidStack = null;
    }

    public ModelDarkTank(IBakedModel baseModel, int capacity, FluidStack fluidStack,
                         IBlockState blockState, EnumFacing facing, long rand) {
        super(baseModel, blockState, facing, rand);
        this.capacity = capacity;
        this.fluidStack = fluidStack;
    }

    public ModelDarkTank(IBakedModel baseModel, int capacity, FluidStack fluidStack,
                         ItemStack itemStack, World world, EntityLivingBase entity) {
        super(baseModel, itemStack, world, entity);
        this.capacity = capacity;
        this.fluidStack = fluidStack;
    }

    protected IBlockTank getBlockTank(ItemStack itemStack) {
        return DarkTank.getInstance();
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

    @Override
    public IBakedModel handleBlockState(IBlockState state, EnumFacing side, long rand) {
        int capacity = BlockHelpers.getSafeBlockStateProperty((IExtendedBlockState) state, DarkTank.TANK_CAPACITY, 0);
        FluidStack fluidStack = BlockHelpers.getSafeBlockStateProperty((IExtendedBlockState) state, DarkTank.TANK_FLUID, null);
        return new ModelDarkTank(baseModel, capacity, fluidStack, state, side, rand);
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack, World world, EntityLivingBase entity) {
        final IBlockTank tank = getBlockTank(itemStack);
        if(itemStack != null && itemStack.getTagCompound() != null) {
            int capacity = tank.getTankCapacity(itemStack);
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound().getCompoundTag(tank.getTankNBTName()));
            return new ModelDarkTank(baseModel, capacity, fluidStack, itemStack, world, entity);
        }
        return new ModelDarkTank(baseModel, 0, null, itemStack, world, entity);
    }

    protected List<BakedQuad> getFluidQuads(FluidStack fluidStack, int capacity) {
        float height = Math.min(0.99F, ((float) fluidStack.amount / (float) capacity)) / 1.01F;
        List<BakedQuad> quads = Lists.newArrayList();
        for(EnumFacing side : EnumFacing.VALUES) {
            TextureAtlasSprite texture = org.cyclops.cyclopscore.helper.RenderHelpers.getFluidIcon(fluidStack, side);
            int color = RenderHelpers.getFluidBakedQuadColor(fluidStack);
            if(side == EnumFacing.UP) {
                addBakedQuadRotated(quads, 0.13F, 0.87F, 0.13F, 0.87F, height, texture, side, ROTATION_FIX[side.ordinal()], true, color, ROTATION_UV);
            } else if(side == EnumFacing.DOWN) {
                addBakedQuadRotated(quads, 0.13F, 0.87F, 0.13F, 0.87F, 0.95F, texture, side, ROTATION_FIX[side.ordinal()], true, color, ROTATION_UV);
            } else {
                float x1 = 0.13F;
                float x2 = 0.87F;
                float z1 = 0.01F;
                float z2 = height;
                if(side == EnumFacing.EAST || side == EnumFacing.SOUTH) {
                    z1 = 0.99F - height;
                    z2 = 0.99F;
                }
                if(side == EnumFacing.EAST || side == EnumFacing.WEST) {
                    float tmp1 = x1;
                    float tmp2 = x2;
                    x1 = z1;
                    x2 = z2;
                    z1 = tmp1;
                    z2 = tmp2;
                }
                float[][] uvs;
                if(side == EnumFacing.UP || side == EnumFacing.DOWN) {
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
}
