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
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.cyclops.evilcraft.Reference;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

/**
 * Model the box of eternal closure.
 * @author rubensworks
 */
public class ModelBoxOfEternalClosure implements UnbakedModel, IUnbakedGeometry<ModelBoxOfEternalClosure> {

    public static ResourceLocation boxModel = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/box");
    public static ResourceLocation boxLidModel = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/box_lid");
    public static ResourceLocation boxLidRotatedModel = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/box_lid_rotated");

    @Override
    public Collection<ResourceLocation> getDependencies() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.add(boxModel);
        builder.add(boxLidModel);
        builder.add(boxLidRotatedModel);
        return builder.build();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {

    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState) {
        ModelBoxOfEternalClosureBaked bakedModel = new ModelBoxOfEternalClosureBaked();

        try {
            ModelBoxOfEternalClosureBaked.boxModel = bakery.bake(boxModel, modelState, spriteGetter);
            ModelBoxOfEternalClosureBaked.boxLidModel = bakery.bake(boxLidModel, modelState, spriteGetter);
            ModelBoxOfEternalClosureBaked.boxLidRotatedModel = bakery.bake(boxLidRotatedModel, modelState, spriteGetter);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bakedModel;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
        return bake(baker, spriteGetter, modelState);
    }

}
