package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosure;

/**
 * Custom model loader for the box of eternal closure.
 * @author rubensworks
 */
public class ModelLoaderBoxOfEternalClosure implements IGeometryLoader<ModelBoxOfEternalClosure> {

    @Override
    public ModelBoxOfEternalClosure read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new ModelBoxOfEternalClosure();
    }
}
