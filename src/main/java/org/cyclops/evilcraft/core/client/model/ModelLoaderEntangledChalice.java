package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;
import org.cyclops.evilcraft.client.render.model.ModelEntangledChalice;
import org.cyclops.evilcraft.client.render.model.ModelEntangledChaliceBaked;

/**
 * Custom model loader for the entangled chalice.
 * @author rubensworks
 */
public class ModelLoaderEntangledChalice implements IModelLoader<ModelEntangledChalice> {

    @Override
    public IResourceType getResourceType() {
        return VanillaResourceType.MODELS;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public ModelEntangledChalice read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        ModelEntangledChalice model = new ModelEntangledChalice();
        ModelLoader.addSpecialModel(ModelEntangledChaliceBaked.chaliceModelName);
        ModelLoader.addSpecialModel(ModelEntangledChaliceBaked.gemsModelName);
        return model;
    }
}
