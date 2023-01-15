package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

/**
 * Model the entangled chalice.
 * @author rubensworks
 */
public class ModelEntangledChalice implements UnbakedModel, IUnbakedGeometry<ModelEntangledChalice> {

    @Override
    public Collection<ResourceLocation> getDependencies() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.add(ModelEntangledChaliceBaked.chaliceModelName);
        builder.add(ModelEntangledChaliceBaked.gemsModelName);
        return builder.build();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {

    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
        ModelEntangledChaliceBaked bakedModel = new ModelEntangledChaliceBaked();

        try {
            ModelEntangledChaliceBaked.chaliceModel = bakery.bake(ModelEntangledChaliceBaked.chaliceModelName, modelState, spriteGetter);
            ModelEntangledChaliceBaked.gemsModel = bakery.bake(ModelEntangledChaliceBaked.gemsModelName, modelState, spriteGetter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bakedModel;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        return bake(baker, spriteGetter, modelState, modelLocation);
    }

}
