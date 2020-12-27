package org.cyclops.evilcraft.core.client.model;

import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometryPart;

import javax.annotation.Nullable;

/**
 * Model configuration that wraps over another configuration.
 * @author rubensworks
 */
public class ModelConfigurationWrapper implements IModelConfiguration {

    private final IModelConfiguration wrapped;

    public ModelConfigurationWrapper(IModelConfiguration wrapped) {
        this.wrapped = wrapped;
    }

    @Nullable
    @Override
    public IUnbakedModel getOwnerModel() {
        return this.wrapped.getOwnerModel();
    }

    @Override
    public String getModelName() {
        return this.wrapped.getModelName();
    }

    @Override
    public boolean isTexturePresent(String name) {
        return this.wrapped.isTexturePresent(name);
    }

    @Override
    public RenderMaterial resolveTexture(String name) {
        return this.wrapped.resolveTexture(name);
    }

    @Override
    public boolean isShadedInGui() {
        return this.wrapped.isShadedInGui();
    }

    @Override
    public boolean isSideLit() {
        return this.wrapped.isSideLit();
    }

    @Override
    public boolean useSmoothLighting() {
        return this.wrapped.useSmoothLighting();
    }

    @Override
    public ItemCameraTransforms getCameraTransforms() {
        return this.wrapped.getCameraTransforms();
    }

    @Override
    public IModelTransform getCombinedTransform() {
        return this.wrapped.getCombinedTransform();
    }

    @Override
    public boolean getPartVisibility(IModelGeometryPart part) {
        return this.wrapped.getPartVisibility(part);
    }

    @Override
    public boolean getPartVisibility(IModelGeometryPart part, boolean fallback) {
        return this.wrapped.getPartVisibility(part, fallback);
    }
}
