package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.IModelLoader;
import org.cyclops.evilcraft.client.render.model.ModelBoxOfEternalClosure;

/**
 * Custom model loader for the box of eternal closure.
 * @author rubensworks
 */
public class ModelLoaderBoxOfEternalClosure implements IModelLoader<ModelBoxOfEternalClosure> {

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public ModelBoxOfEternalClosure read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        ModelBoxOfEternalClosure model = new ModelBoxOfEternalClosure();
        ForgeModelBakery.addSpecialModel(ModelBoxOfEternalClosure.boxModel);
        ForgeModelBakery.addSpecialModel(ModelBoxOfEternalClosure.boxLidModel);
        ForgeModelBakery.addSpecialModel(ModelBoxOfEternalClosure.boxLidRotatedModel);
        return model;
    }
}
