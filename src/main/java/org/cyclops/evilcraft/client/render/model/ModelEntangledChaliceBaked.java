package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.cyclops.cyclopscore.client.model.DynamicBaseModel;
import org.cyclops.evilcraft.tileentity.TileEntangledChalice;

import java.util.List;

/**
 * A baked entangled chalice model.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ModelEntangledChaliceBaked extends DynamicBaseModel implements ISmartItemModel {

    public static IBakedModel chaliceModel;

    private final FluidStack fluidStack;

    public ModelEntangledChaliceBaked() {
        fluidStack = null;
    }

    public ModelEntangledChaliceBaked(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        List<BakedQuad> quads = Lists.newLinkedList();

        quads.addAll(chaliceModel.getGeneralQuads());
        if(fluidStack != null) {
            quads.addAll(getFluidQuads(fluidStack, TileEntangledChalice.BASE_CAPACITY));
        }

        return quads;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IBakedModel handleItemState(ItemStack itemStack) {
        return new ModelEntangledChaliceBaked(((IFluidContainerItem) itemStack.getItem()).getFluid(itemStack));
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return chaliceModel.getParticleTexture();
    }

    protected List<BakedQuad> getFluidQuads(FluidStack fluidStack, int capacity) {
        float height = Math.min(0.95F, ((float) fluidStack.amount / (float) capacity)) * 0.1875F + 0.8125F;
        List<BakedQuad> quads = Lists.newArrayList();
        TextureAtlasSprite texture = org.cyclops.cyclopscore.helper.RenderHelpers.getFluidIcon(fluidStack, EnumFacing.UP);
        addBakedQuadRotated(quads, 0.1875F, 0.8125F, 0.1875F, 0.8125F, height, texture, EnumFacing.UP, ROTATION_FIX[EnumFacing.UP.ordinal()]);
        return quads;
    }
}
