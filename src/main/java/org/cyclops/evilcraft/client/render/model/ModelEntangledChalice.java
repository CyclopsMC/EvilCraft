package org.cyclops.evilcraft.client.render.model;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;

/**
 * Model the entangled chalice.
 * @author rubensworks
 */
public class ModelEntangledChalice implements UnbakedModel {

    @Override
    public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms) {
        ModelEntangledChaliceBaked bakedModel = new ModelEntangledChaliceBaked();

        try {
            ModelEntangledChaliceBaked.chaliceModel = baker.bake(ModelEntangledChaliceBaked.chaliceModelName, modelState);
            ModelEntangledChaliceBaked.gemsModel = baker.bake(ModelEntangledChaliceBaked.gemsModelName, modelState);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bakedModel;
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        resolver.resolve(ModelEntangledChaliceBaked.chaliceModelName);
        resolver.resolve(ModelEntangledChaliceBaked.gemsModelName);
    }
}
