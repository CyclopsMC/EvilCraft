package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

/**
 * Custom model loader for broom part items.
 * @author rubensworks
 */
public class ModelLoaderBroomPart implements IGeometryLoader<BroomPartModel> {

    @Override
    public BroomPartModel read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new BroomPartModel();
    }
}
