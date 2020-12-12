package org.cyclops.evilcraft.core.client.model;

import net.minecraft.client.renderer.model.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoaderRegistry;

/**
 * Model configuration that has a texture override.
 * @author rubensworks
 */
public class ModelConfigurationRetextured extends ModelConfigurationWrapper {
    private final Material material;

    public ModelConfigurationRetextured(IModelConfiguration wrapped, ResourceLocation texture) {
        super(wrapped);
        this.material = ModelLoaderRegistry.blockMaterial(texture);
    }

    @Override
    public boolean isTexturePresent(String name) {
        return super.isTexturePresent(name);
    }

    @Override
    public Material resolveTexture(String name) {
        return material;
    }
}
