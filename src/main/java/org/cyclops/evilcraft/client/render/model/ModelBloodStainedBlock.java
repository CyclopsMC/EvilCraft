package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.cyclops.cyclopscore.client.model.DynamicModel;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.evilcraft.block.BloodStainedBlock;

import java.util.List;

/**
 * Dynamic model for blood stained blocks.
 * @author rubensworks
 */
public class ModelBloodStainedBlock extends DynamicModel {

    private final IBakedModel baseModel;
    private final TextureAtlasSprite overlayIcon;

    public ModelBloodStainedBlock() {
        this.baseModel = null;
        this.overlayIcon = null;
    }

    public ModelBloodStainedBlock(IBakedModel baseModel, TextureAtlasSprite overlayIcon) {
        this.baseModel = baseModel;
        this.overlayIcon = overlayIcon;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side) {
        List<BakedQuad> quads = Lists.newArrayList(baseModel.getFaceQuads(side));
        if(side == EnumFacing.UP) {
            addBakedQuad(quads, 0, 1, 0, 1, 1, overlayIcon, EnumFacing.UP);
        }
        return quads;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BakedQuad> getGeneralQuads() {
        List<BakedQuad> quads = Lists.newArrayList(baseModel.getGeneralQuads());
        addBakedQuad(quads, 0, 1, 0, 1, 1, overlayIcon, EnumFacing.UP);
        return quads;
    }

    protected TextureAtlasSprite getIcon(BlockPos pos) {
        TextureAtlasSprite[] icons = new TextureAtlasSprite[3];
        icons[0] = BloodStainedBlock.getInstance().icon0;
        icons[1] = BloodStainedBlock.getInstance().icon1;
        icons[2] = BloodStainedBlock.getInstance().icon2;
        return icons[pos == null ? 0 : Math.abs(pos.hashCode()) % 3];
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        IBlockState blockState = BlockHelpers.getSafeBlockStateProperty(extendedBlockState, BloodStainedBlock.INNERBLOCK, null);
        BlockPos pos = BlockHelpers.getSafeBlockStateProperty(extendedBlockState, BloodStainedBlock.POS, null);
        TextureAtlasSprite overlayIcon = getIcon(pos);
        IBakedModel baseModel = RenderHelpers.getBakedModel(blockState);
        return new ModelBloodStainedBlock(baseModel, overlayIcon);
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        return null;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return RenderHelpers.getBakedModel(Blocks.stone.getDefaultState()).getParticleTexture();
    }
}
