package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.client.model.DynamicChildItemModel;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.block.DarkTank;
import org.cyclops.evilcraft.core.block.IBlockTank;

import java.util.List;

/**
 * The dynamic item model for the dark tank.
 * @author rubensworks
 */
public class ModelDarkTank extends DynamicChildItemModel implements ISmartBlockModel {

    private final int capacity;
    private final FluidStack fluidStack;

    public ModelDarkTank(IBakedModel baseModel) {
        super(baseModel);
        this.capacity = 0;
        this.fluidStack = null;
    }

    public ModelDarkTank(IBakedModel baseModel, int capacity, FluidStack fluidStack) {
        super(baseModel);
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
        combinedList.addAll(getBaseModel().getGeneralQuads());
        return combinedList;
    }

    protected List<BakedQuad> getFluidQuads(FluidStack fluidStack, int capacity) {
        float height = Math.min(0.99F, ((float) fluidStack.amount / (float) capacity)) / 1.01F;
        List<BakedQuad> quads = Lists.newArrayList();
        for(EnumFacing side : EnumFacing.VALUES) {
            TextureAtlasSprite texture = org.cyclops.cyclopscore.helper.RenderHelpers.getFluidIcon(fluidStack, side);
            if(side == EnumFacing.UP) {
                addBakedQuadRotated(quads, 0.13F, 0.87F, 0.13F, 0.87F, height, texture, side, ROTATION_FIX[side.ordinal()]);
            } else if(side == EnumFacing.DOWN) {
                addBakedQuadRotated(quads, 0.13F, 0.87F, 0.13F, 0.87F, 0.95F, texture, side, ROTATION_FIX[side.ordinal()]);
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
                addBakedQuadRotated(quads, x1, x2, z1, z2, 0.87F, texture, side, ROTATION_FIX[side.ordinal()], true, uvs);
            }
        }
        return quads;
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack) {
        final IBlockTank tank = getBlockTank(itemStack);
        if(itemStack.getTagCompound() != null) {
            int capacity = tank.getTankCapacity(itemStack);
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound().getCompoundTag(tank.getTankNBTName()));
            return new ModelDarkTank(getBaseModel(), capacity, fluidStack);
        }
        return this;
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        int capacity = BlockHelpers.getSafeBlockStateProperty((IExtendedBlockState) state, DarkTank.TANK_CAPACITY, 0);
        FluidStack fluidStack = BlockHelpers.getSafeBlockStateProperty((IExtendedBlockState) state, DarkTank.TANK_FLUID, null);
        return new ModelDarkTank(getBaseModel(), capacity, fluidStack);
    }
}
