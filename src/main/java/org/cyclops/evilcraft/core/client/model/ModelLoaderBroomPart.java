package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;

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
        // TODO: rm?
        // modelContents.remove("loader");
        // BlockModel modelBlock = deserializationContext.deserialize(modelContents, BlockModel.class);
        BroomPartModel model = new BroomPartModel();
        return model;
    }
}