package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.IModelLoader;
import org.cyclops.evilcraft.client.render.model.ModelEntangledChalice;
import org.cyclops.evilcraft.client.render.model.ModelEntangledChaliceBaked;

/**
 * Custom model loader for the entangled chalice.
 * @author rubensworks
 */
public class ModelLoaderEntangledChalice implements IModelLoader<ModelEntangledChalice> {

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public ModelEntangledChalice read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        ModelEntangledChalice model = new ModelEntangledChalice();
        ForgeModelBakery.addSpecialModel(ModelEntangledChaliceBaked.chaliceModelName);
        ForgeModelBakery.addSpecialModel(ModelEntangledChaliceBaked.gemsModelName);
        return model;
    }
}
