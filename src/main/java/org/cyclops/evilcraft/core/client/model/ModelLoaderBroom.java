package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

/**
 * Custom model loader for the broom item.
 * @author rubensworks
 */
public class ModelLoaderBroom implements UnbakedModelLoader<BroomModel> {

    @Override
    public BroomModel read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new BroomModel();
    }
}
