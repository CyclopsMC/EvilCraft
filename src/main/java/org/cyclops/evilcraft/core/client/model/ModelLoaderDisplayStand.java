package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;
import org.cyclops.evilcraft.client.render.model.ModelDisplayStand;

/**
 * Custom model loader for the display stand.
 * @author rubensworks
 */
public class ModelLoaderDisplayStand implements IModelLoader<ModelDisplayStand> {

    @Override
    public IResourceType getResourceType() {
        return VanillaResourceType.MODELS;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public ModelDisplayStand read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        modelContents.remove("loader");
        BlockModel modelBlock = deserializationContext.deserialize(modelContents, BlockModel.class);
        ModelDisplayStand model = new ModelDisplayStand(modelBlock);
        return model;
    }
}
