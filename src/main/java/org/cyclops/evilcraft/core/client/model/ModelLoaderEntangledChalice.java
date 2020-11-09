package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;
import org.cyclops.evilcraft.client.render.model.ModelEntangledChalice;

/**
 * Custom model loader for the entangled chalice.
 * @author rubensworks
 */
public class ModelLoaderEntangledChalice implements IModelLoader<ModelEntangledChalice> {

    @Override
    public IResourceType getResourceType() {
        return VanillaResourceType.MODELS;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public ModelEntangledChalice read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        // TODO: rm?
        // modelContents.remove("loader");
        // BlockModel modelBlock = deserializationContext.deserialize(modelContents, BlockModel.class);
        ModelEntangledChalice model = new ModelEntangledChalice();
        return model;
    }
}
