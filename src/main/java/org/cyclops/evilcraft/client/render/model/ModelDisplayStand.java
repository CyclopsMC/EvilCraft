package org.cyclops.evilcraft.client.render.model;

import net.minecraft.client.renderer.block.model.BlockModel;
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

import java.util.Collection;
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
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {
        this.blockModel.resolveParents(resolver);
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return this.blockModel.getDependencies();
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new ModelDisplayStandBaked(this.blockModel, this.blockModel.bake(bakery, spriteGetter, modelState, modelLocation), context, modelState, spriteGetter);
    }

    @Override
    public BakedModel bake(ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
        throw new UnsupportedOperationException("Use the other bake implementation!");
    }

}
