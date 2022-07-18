package org.cyclops.evilcraft.client.render.model;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

/**
 * Model for the display stand.
 * @author rubensworks
 */
public class ModelDisplayStand implements UnbakedModel, IUnbakedGeometry<ModelDisplayStand> {

    private final BlockModel blockModel;

    public ModelDisplayStand(BlockModel blockModel) {
        this.blockModel = blockModel;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return this.blockModel.getDependencies();
    }

    @Override
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return this.blockModel.getMaterials(modelGetter, missingTextureErrors);
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter,
                                 ModelState transform, ResourceLocation location) {
        throw new UnsupportedOperationException("Use bake instead");
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter,
                            ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new ModelDisplayStandBaked(this.blockModel, this.blockModel.bake(bakery, spriteGetter, modelTransform, modelLocation), context, modelTransform);
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return getMaterials(modelGetter, missingTextureErrors);
    }

}
