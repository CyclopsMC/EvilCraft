package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;

/**
 * Custom model loader for the broom item.
 * @author rubensworks
 */
public class ModelLoaderBroom implements IModelLoader<BroomModel> {

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public BroomModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new BroomModel();
    }
}
