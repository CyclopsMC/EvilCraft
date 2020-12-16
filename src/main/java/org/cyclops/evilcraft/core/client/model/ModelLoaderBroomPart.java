package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;
import org.cyclops.evilcraft.core.broom.BroomParts;

/**
 * Custom model loader for broom part items.
 * @author rubensworks
 */
public class ModelLoaderBroomPart implements IModelLoader<BroomPartModel> {
    @Override
    public IResourceType getResourceType() {
        return VanillaResourceType.MODELS;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public BroomPartModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        BroomPartModel model = new BroomPartModel();
        for (ResourceLocation partModel : BroomParts.REGISTRY.getPartModels()) {
            ModelLoader.addSpecialModel(partModel);
        }
        return model;
    }
}
