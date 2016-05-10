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
import org.cyclops.evilcraft.block.BoxOfEternalClosureConfig;

import java.util.Collection;
import java.util.Collections;

/**
 * Model the box of eternal closure.
 * @author rubensworks
 */
public class ModelBoxOfEternalClosure implements IModel {

    @Override
    public Collection<ResourceLocation> getDependencies() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.add(BoxOfEternalClosureConfig.boxModel);
        builder.add(BoxOfEternalClosureConfig.boxLidModel);
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
        ModelBoxOfEternalClosureBaked bakedModel = new ModelBoxOfEternalClosureBaked();

        try {
            IModel boxModel = ModelLoaderRegistry.getModel(BoxOfEternalClosureConfig.boxModel);
            ModelBoxOfEternalClosureBaked.boxModel = boxModel.bake(state, format, bakedTextureGetter);
            IModel boxLidModel = ModelLoaderRegistry.getModel(BoxOfEternalClosureConfig.boxLidModel);
            ModelBoxOfEternalClosureBaked.boxLidModel = boxLidModel.bake(state, format, bakedTextureGetter);
            IModel boxLidRotatedModel = ModelLoaderRegistry.getModel(BoxOfEternalClosureConfig.boxLidRotatedModel);
            ModelBoxOfEternalClosureBaked.boxLidRotatedModel = boxLidRotatedModel.bake(state, format, bakedTextureGetter);
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
