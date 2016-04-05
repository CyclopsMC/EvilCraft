package org.cyclops.evilcraft.core.client.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import java.util.Collection;
import java.util.Collections;

/**
 * Model for a variant of a variable item.
 * @author rubensworks
 */
public class BroomModel implements IModel {

    @Override
    public Collection<ResourceLocation> getDependencies() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.addAll(BroomParts.REGISTRY.getPartModels());
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ResourceLocation> getTextures() {
        return Collections.emptyList();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format,
                                    Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        BroomModelBaked bakedModel = new BroomModelBaked();

        // Add aspects to baked model.
        for(IBroomPart part : BroomParts.REGISTRY.getParts()) {
            try {
                IModel model = ModelLoaderRegistry.getModel(BroomParts.REGISTRY.getPartModel(part));
                IBakedModel bakedAspectModel = model.bake(state, format, bakedTextureGetter);
                bakedModel.addBroomModel(part, bakedAspectModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bakedModel;
    }

    @Override
    public IModelState getDefaultState() {
        return ModelRotation.X0_Y0;
    }

}
