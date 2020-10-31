package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosure;

/**
 * Custom model loader for the broom item.
 * @author rubensworks
 */
public class ModelLoaderBoxOfEternalClosure implements IModelLoader<ModelBoxOfEternalClosure> {

    @Override
    public IResourceType getResourceType() {
        return VanillaResourceType.MODELS;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public ModelBoxOfEternalClosure read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        // modelContents.remove("loader");
        // BlockModel modelBlock = deserializationContext.deserialize(modelContents, BlockModel.class);
        ModelBoxOfEternalClosure model = new ModelBoxOfEternalClosure();
        return model;
    }
}
