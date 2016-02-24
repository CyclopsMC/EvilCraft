package org.cyclops.evilcraft.client.render.model;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import org.cyclops.evilcraft.block.EntangledChaliceConfig;

import java.io.IOException;
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
        return builder.build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<ResourceLocation> getTextures() {
        return Collections.emptyList();
    }

    @Override
    public IFlexibleBakedModel bake(IModelState state, VertexFormat format,
                                    Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        ModelEntangledChaliceBaked bakedModel = new ModelEntangledChaliceBaked();

        try {
            IModel chaliceModel = ModelLoaderRegistry.getModel(EntangledChaliceConfig.chaliceModel);
            ModelEntangledChaliceBaked.chaliceModel = chaliceModel.bake(state, format, bakedTextureGetter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bakedModel;
    }

    @Override
    public IModelState getDefaultState() {
        return ModelRotation.X0_Y0;
    }

}
