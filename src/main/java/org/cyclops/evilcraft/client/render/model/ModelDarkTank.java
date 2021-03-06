package org.cyclops.evilcraft.client.render.model;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.BlockModel;
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

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

/**
 * Model for the dark tank.
 * @author rubensworks
 */
public class ModelDarkTank implements IUnbakedModel, IModelGeometry<ModelDarkTank> {

    private final BlockModel blockModel;

    public ModelDarkTank(BlockModel blockModel) {
        this.blockModel = blockModel;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return this.blockModel.getDependencies();
    }

    @Override
    public Collection<RenderMaterial> getTextures(Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return this.blockModel.getTextures(modelGetter, missingTextureErrors);
    }

    @Nullable
    @Override
    public IBakedModel bakeModel(ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter,
                                 IModelTransform transform, ResourceLocation location) {
        return new ModelDarkTankBaked(this.blockModel.bakeModel(bakery, spriteGetter, transform, location));
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
