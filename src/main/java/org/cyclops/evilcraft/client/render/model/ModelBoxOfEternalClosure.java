package org.cyclops.evilcraft.client.render.model;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.evilcraft.Reference;

/**
 * Model the box of eternal closure.
 * @author rubensworks
 */
public class ModelBoxOfEternalClosure implements UnbakedModel {

    public static ResourceLocation boxModel = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/box");
    public static ResourceLocation boxLidModel = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/box_lid");
    public static ResourceLocation boxLidRotatedModel = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "block/box_lid_rotated");

    @Override
    public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms) {
        ModelBoxOfEternalClosureBaked bakedModel = new ModelBoxOfEternalClosureBaked();

        try {
            ModelBoxOfEternalClosureBaked.boxModel = baker.bake(boxModel, modelState);
            ModelBoxOfEternalClosureBaked.boxLidModel = baker.bake(boxLidModel, modelState);
            ModelBoxOfEternalClosureBaked.boxLidRotatedModel = baker.bake(boxLidRotatedModel, modelState);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bakedModel;
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        resolver.resolve(boxModel);
        resolver.resolve(boxLidModel);
        resolver.resolve(boxLidRotatedModel);
    }
}
