package org.cyclops.evilcraft.core.client.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A baked broom part model.
 * @author rubensworks
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class BroomPartModelBaked extends DynamicItemAndBlockModel {

    private final Map<IBroomPart, IBakedModel> broomPartModels = Maps.newHashMap();
    private final List<BakedQuad> quads;

    public BroomPartModelBaked() {
        super(true, false);
        this.quads = Collections.emptyList();
    }

    public BroomPartModelBaked(List<BakedQuad> quads) {
        super(false, true);
        this.quads = Objects.requireNonNull(quads);
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return this.quads;
    }

    public void addBroomPartModel(IBroomPart part, IBakedModel bakedModel) {
        broomPartModels.put(part, bakedModel);
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state, EnumFacing side, long rand) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IBakedModel handleItemState(ItemStack itemStack, World world, EntityLivingBase entity) {
        List<BakedQuad> quads = Lists.newLinkedList();

        IBroomPart part = BroomParts.REGISTRY.getPartFromItem(itemStack);
        IBakedModel model = broomPartModels.get(part);
        if (model != null) {
            quads.addAll(color(model.getQuads(null, getRenderingSide(), 0L), part.getModelColor()));
        }

        return new BroomPartModelBaked(quads);
    }

    /**
     * Color the quads
     * @param quads The original quads
     * @param color The color
     * @return The colored quads
     */
    private Collection<? extends BakedQuad> color(List<BakedQuad> quads, int color) {
        List<BakedQuad> offsetQuads = Lists.newArrayListWithExpectedSize(quads.size());
        for (BakedQuad quad : quads) {
            int[] vertexData = Arrays.copyOf(quad.getVertexData(), quad.getVertexData().length);
            for(int i = 0; i < vertexData.length / 7; i++) {
                vertexData[i * 7 + 3] = color;
            }
            offsetQuads.add(new BakedQuad(vertexData, quad.getTintIndex(), quad.getFace(), quad.getSprite(), false, DefaultVertexFormats.ITEM));
        }
        return offsetQuads;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }
}
