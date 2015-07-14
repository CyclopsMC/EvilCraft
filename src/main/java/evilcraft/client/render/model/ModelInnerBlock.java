package evilcraft.client.render.model;

import evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.cyclops.cyclopscore.client.model.DynamicModel;

import java.util.List;

/**
 * Dynamic model for {@link evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocks}.
 * @author rubensworks
 */
public class ModelInnerBlock extends DynamicModel {

    private final ConfigurableBlockWithInnerBlocks block;
    private final IBakedModel baseModel;

    public ModelInnerBlock(ConfigurableBlockWithInnerBlocks block) {
        this.block = block;
        this.baseModel = null;
    }

    public ModelInnerBlock(ConfigurableBlockWithInnerBlocks block, IBakedModel baseModel) {
        this.block = block;
        this.baseModel = baseModel;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side) {
        return baseModel.getFaceQuads(side);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getGeneralQuads() {
        return baseModel.getGeneralQuads();
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        IBlockState innerBlockState = block.getBlockFromState(state);
        IBakedModel baseModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getModelForState(innerBlockState);
        return new ModelInnerBlock(block, baseModel);
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        IBlockState innerBlockState = block.getBlockFromMeta(stack.getItemDamage());
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getModelForState(innerBlockState);
    }

    @Override
    public TextureAtlasSprite getTexture() {
        return null;
    }
}
