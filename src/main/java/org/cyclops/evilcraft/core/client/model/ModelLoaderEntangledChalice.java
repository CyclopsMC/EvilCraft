package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;
import org.cyclops.evilcraft.client.render.model.ModelEntangledChalice;

/**
 * Custom model loader for the entangled chalice.
 * @author rubensworks
 */
public class ModelLoaderEntangledChalice implements UnbakedModelLoader<ModelEntangledChalice> {

    @Override
    public ModelEntangledChalice read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        return new ModelEntangledChalice();
    }
}
