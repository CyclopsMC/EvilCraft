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
import org.cyclops.evilcraft.Reference;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

/**
 * Model the box of eternal closure.
 * @author rubensworks
 */
public class ModelBoxOfEternalClosure implements IUnbakedModel, IModelGeometry<ModelBoxOfEternalClosure> {

    public static ResourceLocation boxModel = new ResourceLocation(Reference.MOD_ID, "block/box");
    public static ResourceLocation boxLidModel = new ResourceLocation(Reference.MOD_ID, "block/box_lid");
    public static ResourceLocation boxLidRotatedModel = new ResourceLocation(Reference.MOD_ID, "block/box_lid_rotated");

    @Override
    public Collection<ResourceLocation> getDependencies() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.add(boxModel);
        builder.add(boxLidModel);
        builder.add(boxLidRotatedModel);
        return builder.build();
    }

    @Override
    public Collection<RenderMaterial> getTextures(Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public IBakedModel bakeModel(ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter,
                                 IModelTransform transform, ResourceLocation location) {
        ModelBoxOfEternalClosureBaked bakedModel = new ModelBoxOfEternalClosureBaked();

        try {
            ModelBoxOfEternalClosureBaked.boxModel = bakery.getBakedModel(boxModel, transform, spriteGetter);
            ModelBoxOfEternalClosureBaked.boxLidModel = bakery.getBakedModel(boxLidModel, transform, spriteGetter);
            ModelBoxOfEternalClosureBaked.boxLidRotatedModel = bakery.getBakedModel(boxLidRotatedModel, transform, spriteGetter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bakedModel;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        return bakeModel(bakery, spriteGetter, modelTransform, modelLocation);
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return getTextures(modelGetter, missingTextureErrors);
    }

}
