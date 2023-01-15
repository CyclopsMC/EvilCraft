package org.cyclops.evilcraft.core.client.model;

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
import org.cyclops.evilcraft.api.broom.IBroomPart;
import org.cyclops.evilcraft.core.broom.BroomParts;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

/**
 * Model for a variant of a broom part item.
 * @author rubensworks
 */
public class BroomPartModel implements UnbakedModel, IUnbakedGeometry<BroomPartModel> {

    @Override
    public Collection<ResourceLocation> getDependencies() {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        builder.addAll(BroomParts.REGISTRY.getPartModels());
        return builder.build();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resolver) {

    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
        BroomPartModelBaked bakedModel = new BroomPartModelBaked();

        // Add aspects to baked model.
        for(IBroomPart part : BroomParts.REGISTRY.getParts()) {
            try {
                BakedModel bakedAspectModel = bakery.bake(BroomParts.REGISTRY.getPartModel(part), modelState, spriteGetter);
                bakedModel.addBroomPartModel(part, bakedAspectModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bakedModel;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        return bake(baker, spriteGetter, modelState, modelLocation);
    }
}
