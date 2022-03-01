package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import org.cyclops.evilcraft.client.render.model.ModelDisplayStand;

/**
 * Custom model loader for the display stand.
 * @author rubensworks
 */
public class ModelLoaderDisplayStand implements IModelLoader<ModelDisplayStand> {

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public ModelDisplayStand read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        modelContents.remove("loader");
        BlockModel modelBlock = deserializationContext.deserialize(modelContents, BlockModel.class);
        ModelDisplayStand model = new ModelDisplayStand(modelBlock);
        return model;
    }
}
