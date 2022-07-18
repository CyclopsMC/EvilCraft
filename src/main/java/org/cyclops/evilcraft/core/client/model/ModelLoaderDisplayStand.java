package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.cyclops.evilcraft.client.render.model.ModelDisplayStand;

/**
 * Custom model loader for the display stand.
 * @author rubensworks
 */
public class ModelLoaderDisplayStand implements IGeometryLoader<ModelDisplayStand> {

    @Override
    public ModelDisplayStand read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        modelContents.remove("loader");
        BlockModel modelBlock = deserializationContext.deserialize(modelContents, BlockModel.class);
        ModelDisplayStand model = new ModelDisplayStand(modelBlock);
        return model;
    }
}
