package org.cyclops.evilcraft.client.render.model;

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
import org.cyclops.evilcraft.block.EntangledChaliceConfig;

import java.util.Collection;
import java.util.Collections;

/**
 * Model the entangled chalice.
 * @author rubensworks
 */
public class ModelEntangledChalice implements IModel {

    @Override
    public Collection<ResourceLocation> getDependencies() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.add(EntangledChaliceConfig.chaliceModel);
        builder.add(EntangledChaliceConfig.gemsModel);
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
        ModelEntangledChaliceBaked bakedModel = new ModelEntangledChaliceBaked();

        try {
            IModel chaliceModel = ModelLoaderRegistry.getModel(EntangledChaliceConfig.chaliceModel);
            ModelEntangledChaliceBaked.chaliceModel = chaliceModel.bake(state, format, bakedTextureGetter);
            IModel gemsModel = ModelLoaderRegistry.getModel(EntangledChaliceConfig.gemsModel);
            ModelEntangledChaliceBaked.gemsModel = gemsModel.bake(state, format, bakedTextureGetter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bakedModel;
    }

    @Override
    public IModelState getDefaultState() {
        return ModelRotation.X0_Y0;
    }

}
