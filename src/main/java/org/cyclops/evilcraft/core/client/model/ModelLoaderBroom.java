package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

/**
 * Custom model loader for the broom item.
 * @author rubensworks
 */
public class ModelLoaderBroom implements IGeometryLoader<BroomModel> {

    @Override
    public BroomModel read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new BroomModel();
    }
}
