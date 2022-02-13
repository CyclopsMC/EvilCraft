package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import org.cyclops.evilcraft.block.BlockEntangledChaliceConfig;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

/**
 * Model the entangled chalice.
 * @author rubensworks
 */
public class ModelEntangledChalice implements IUnbakedModel, IModelGeometry<ModelEntangledChalice> {

    @Override
    public Collection<ResourceLocation> getDependencies() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.add(ModelEntangledChaliceBaked.chaliceModelName);
        builder.add(ModelEntangledChaliceBaked.gemsModelName);
        return builder.build();
    }

    @Override
    public Collection<RenderMaterial> getMaterials(Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public IBakedModel bake(ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter,
                                 IModelTransform transform, ResourceLocation location) {
        ModelEntangledChaliceBaked bakedModel = new ModelEntangledChaliceBaked();

        try {
            ModelEntangledChaliceBaked.chaliceModel = bakery.getBakedModel(ModelEntangledChaliceBaked.chaliceModelName, transform, spriteGetter);
            ModelEntangledChaliceBaked.gemsModel = bakery.getBakedModel(ModelEntangledChaliceBaked.gemsModelName, transform, spriteGetter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bakedModel;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        return bake(bakery, spriteGetter, modelTransform, modelLocation);
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return getMaterials(modelGetter, missingTextureErrors);
    }

}
