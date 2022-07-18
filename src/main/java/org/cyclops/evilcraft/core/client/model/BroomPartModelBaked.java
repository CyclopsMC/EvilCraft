package org.cyclops.evilcraft.core.client.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.cyclops.cyclopscore.client.model.DynamicItemAndBlockModel;
import org.cyclops.cyclopscore.helper.ModelHelpers;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
public class BroomPartModelBaked extends DynamicItemAndBlockModel {

    private final Map<IBroomPart, BakedModel> broomPartModels = Maps.newHashMap();
    private final List<BakedQuad> quads;
    private final RandomSource rand = RandomSource.create();

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

    public void addBroomPartModel(IBroomPart part, BakedModel bakedModel) {
        broomPartModels.put(part, bakedModel);
    }

    @Override
    public boolean usesBlockLight() {
        return true; // If false, RenderHelper.setupGuiFlatDiffuseLighting() is called
    }

    @Override
    public BakedModel handleBlockState(@Nullable BlockState blockState, @Nullable Direction direction, @Nonnull RandomSource random, @Nonnull ModelData iModelData, @Nullable RenderType renderType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BakedModel handleItemState(ItemStack itemStack, Level world, LivingEntity entity) {
        List<BakedQuad> quads = Lists.newLinkedList();

        IBroomPart part = BroomParts.REGISTRY.getPartFromItem(itemStack);
        BakedModel model = broomPartModels.get(part);
        if (model != null) {
            quads.addAll(color(model.getQuads(null, getRenderingSide(), this.rand), part.getModelColor()));
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
        /*if (true) {
            return quads;
        }*/
        List<BakedQuad> offsetQuads = Lists.newArrayListWithExpectedSize(quads.size());
        for (BakedQuad quad : quads) {
            int[] vertexData = Arrays.copyOf(quad.getVertices(), quad.getVertices().length);
            for(int i = 0; i < vertexData.length / 8; i++) {
                vertexData[i * 8 + 3] = color;
            }
            offsetQuads.add(new BakedQuad(vertexData, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), false));
        }
        return offsetQuads;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return null;
    }

    @Override
    public ItemTransforms getTransforms() {
        return ModelHelpers.DEFAULT_CAMERA_TRANSFORMS;
    }
}
