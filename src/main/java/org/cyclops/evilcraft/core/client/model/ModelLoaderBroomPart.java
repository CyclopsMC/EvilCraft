package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.IModelLoader;
import org.cyclops.evilcraft.core.broom.BroomParts;

/**
 * Custom model loader for broom part items.
 * @author rubensworks
 */
public class ModelLoaderBroomPart implements IModelLoader<BroomPartModel> {
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

    }

    @Override
    public BroomPartModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        BroomPartModel model = new BroomPartModel();
        for (ResourceLocation partModel : BroomParts.REGISTRY.getPartModels()) {
            ForgeModelBakery.addSpecialModel(partModel);
        }
        return model;
    }
}
