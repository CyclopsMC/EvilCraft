package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;

/**
 * Custom model loader for the broom item.
 * @author rubensworks
 */
public class ModelLoaderBroom implements IModelLoader<BroomModel> {

    @Override
    public IResourceType getResourceType() {
        return VanillaResourceType.MODELS;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public BroomModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new BroomModel();
    }
}
