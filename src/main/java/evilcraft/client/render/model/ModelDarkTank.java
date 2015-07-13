package evilcraft.client.render.model;

import com.google.common.collect.Lists;
import evilcraft.block.DarkTank;
import evilcraft.core.block.IBlockTank;
import evilcraft.core.helper.RenderHelpers;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.client.model.DynamicChildItemModel;

import java.util.List;

/**
 * The dynamic item model for the dark tank.
 * @author rubensworks
 */
public class ModelDarkTank extends DynamicChildItemModel {

    private int currentCapacity = 0;
    private FluidStack currentFluidStack = null;

    public ModelDarkTank(IBakedModel baseModel) {
        super(baseModel);
    }

    protected IBlockTank getBlockTank(ItemStack itemStack) {
        return DarkTank.getInstance();
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack) {
        final IBlockTank tank = getBlockTank(itemStack);
        if(itemStack.getTagCompound() != null) {
            this.currentFluidStack = FluidStack.loadFluidStackFromNBT(itemStack.getTagCompound().getCompoundTag(tank.getTankNBTName()));
            this.currentCapacity = tank.getTankCapacity(itemStack);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getGeneralQuads() {
        List<BakedQuad> combinedList = Lists.newArrayList();
        if(currentFluidStack != null) {
            combinedList.addAll(getFluidQuads(currentFluidStack, currentCapacity));
        }
        combinedList.addAll(getBaseModel().getGeneralQuads());
        return combinedList;
    }

    protected List<BakedQuad> getFluidQuads(FluidStack fluidStack, int capacity) {
        float height = Math.min(1.0F, ((float) fluidStack.amount / (float) capacity)) / 1.01F;
        List<BakedQuad> quads = Lists.newArrayList();
        for(EnumFacing side : EnumFacing.VALUES) {
            TextureAtlasSprite texture = RenderHelpers.getFluidIcon(fluidStack, side);
            if(side == EnumFacing.DOWN || side == EnumFacing.UP) {
                addBakedQuadRotated(quads, 0.126F, 0.874F, 0.126F, 0.874F, height, texture, side, ROTATION_FIX[side.ordinal()]);
            } else {
                float x1 = 0.126F;
                float x2 = 0.874F;
                float z1 = 0.001F;
                float z2 = height;
                if(side == EnumFacing.EAST || side == EnumFacing.SOUTH) {
                    z1 = 0.999F - height;
                    z2 = 0.999F;
                }
                if(side == EnumFacing.EAST || side == EnumFacing.WEST) {
                    float tmp1 = x1;
                    float tmp2 = x2;
                    x1 = z1;
                    x2 = z2;
                    z1 = tmp1;
                    z2 = tmp2;
                }
                addBakedQuadRotated(quads, x1, x2, z1, z2, 0.874F, texture, side, ROTATION_FIX[side.ordinal()]);
            }
        }
        return quads;
    }

}
