package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import org.cyclops.evilcraft.client.render.model.ModelDarkTank;

/**
 * Custom model loader for the box of eternal closure.
 * @author rubensworks
 */
public class ModelLoaderDarkTank implements IModelLoader<ModelDarkTank> {

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public ModelDarkTank read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        modelContents.remove("loader");
        BlockModel modelBlock = deserializationContext.deserialize(modelContents, BlockModel.class);
        ModelDarkTank model = new ModelDarkTank(modelBlock);
        return model;
    }
}
