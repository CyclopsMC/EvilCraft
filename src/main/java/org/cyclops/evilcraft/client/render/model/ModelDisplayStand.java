package org.cyclops.evilcraft.client.render.model;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;

/**
 * Model for the display stand.
 * @author rubensworks
 */
public class ModelDisplayStand implements UnbakedModel {

    private final BlockModel blockModel;

    public ModelDisplayStand(BlockModel blockModel) {
        this.blockModel = blockModel;
    }

    @Override
    public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms) {
        return new ModelDisplayStandBaked(this.blockModel, textureSlots, this.blockModel.bake(textureSlots, baker, modelState, hasAmbientOcclusion, useBlockLight, transforms), baker, modelState);
    }

    @Override
    public void resolveDependencies(Resolver resolver) {
        this.blockModel.resolveDependencies(resolver);
    }

    @Override
    public TextureSlots.Data getTextureSlots() {
        return this.blockModel.getTextureSlots();
    }
}
