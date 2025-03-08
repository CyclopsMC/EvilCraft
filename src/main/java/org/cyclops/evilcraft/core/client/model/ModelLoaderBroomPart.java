package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

/**
 * Custom model loader for broom part items.
 * @author rubensworks
 */
public class ModelLoaderBroomPart implements UnbakedModelLoader<BroomPartModel> {

    @Override
    public BroomPartModel read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new BroomPartModel();
    }
}
