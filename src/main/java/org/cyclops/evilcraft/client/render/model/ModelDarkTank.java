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

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

/**
 * Model for the dark tank.
 * @author rubensworks
 */
public class ModelDarkTank implements UnbakedModel, IUnbakedGeometry<ModelDarkTank> {

    private final BlockModel blockModel;

    public ModelDarkTank(BlockModel blockModel) {
        this.blockModel = blockModel;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return this.blockModel.getDependencies();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {
         this.blockModel.resolveParents(resolver);
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
        return new ModelDarkTankBaked(this.blockModel.bake(bakery, spriteGetter, modelState, modelLocation));
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        return bake(baker, spriteGetter, modelState, modelLocation);
    }

}
