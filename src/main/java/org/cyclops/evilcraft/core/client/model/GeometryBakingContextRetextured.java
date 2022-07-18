package org.cyclops.evilcraft.core.client.model;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;

/**
 * Model configuration that has a texture override.
 * @author rubensworks
 */
public class GeometryBakingContextRetextured extends GeometryBakingContextWrapper {
    private final Material material;

    public GeometryBakingContextRetextured(IGeometryBakingContext wrapped, ResourceLocation texture) {
        super(wrapped);
        this.material = new Material(TextureAtlas.LOCATION_BLOCKS, texture);
    }

    @Override
    public boolean hasMaterial(String name) {
        return super.hasMaterial(name);
    }

    @Override
    public Material getMaterial(String name) {
        return material;
    }
}
