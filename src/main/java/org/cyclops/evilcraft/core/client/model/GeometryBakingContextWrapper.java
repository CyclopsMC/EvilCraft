package org.cyclops.evilcraft.core.client.model;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.Nullable;

/**
 * Model configuration that wraps over another configuration.
 * @author rubensworks
 */
public class GeometryBakingContextWrapper implements IGeometryBakingContext {

    private final IGeometryBakingContext wrapped;

    public GeometryBakingContextWrapper(IGeometryBakingContext wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String getModelName() {
        return this.wrapped.getModelName();
    }

    @Override
    public boolean hasMaterial(String name) {
        return this.wrapped.hasMaterial(name);
    }

    @Override
    public Material getMaterial(String name) {
        return this.wrapped.getMaterial(name);
    }

    @Override
    public boolean isGui3d() {
        return this.wrapped.isGui3d();
    }

    @Override
    public boolean useBlockLight() {
        return this.wrapped.useBlockLight();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.wrapped.useAmbientOcclusion();
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.wrapped.getTransforms();
    }

    @Override
    public Transformation getRootTransform() {
        return this.wrapped.getRootTransform();
    }

    @Override
    public @Nullable ResourceLocation getRenderTypeHint() {
        return this.wrapped.getRenderTypeHint();
    }

    @Override
    public boolean isComponentVisible(String component, boolean fallback) {
        return this.wrapped.isComponentVisible(component, fallback);
    }
}
