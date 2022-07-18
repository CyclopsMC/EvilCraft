package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.cyclops.evilcraft.client.render.model.ModelEntangledChalice;

/**
 * Custom model loader for the entangled chalice.
 * @author rubensworks
 */
public class ModelLoaderEntangledChalice implements IGeometryLoader<ModelEntangledChalice> {

    @Override
    public ModelEntangledChalice read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new ModelEntangledChalice();
    }
}
