package org.cyclops.evilcraft.client.render.model;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
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
public class ModelBoxOfEternalClosure implements UnbakedModel, IModelGeometry<ModelBoxOfEternalClosure> {

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
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter,
                                 ModelState transform, ResourceLocation location) {
        ModelBoxOfEternalClosureBaked bakedModel = new ModelBoxOfEternalClosureBaked();

        try {
            ModelBoxOfEternalClosureBaked.boxModel = bakery.bake(boxModel, transform, spriteGetter);
            ModelBoxOfEternalClosureBaked.boxLidModel = bakery.bake(boxLidModel, transform, spriteGetter);
            ModelBoxOfEternalClosureBaked.boxLidRotatedModel = bakery.bake(boxLidRotatedModel, transform, spriteGetter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bakedModel;
    }

    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        return bake(bakery, spriteGetter, modelTransform, modelLocation);
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return getMaterials(modelGetter, missingTextureErrors);
    }

}
