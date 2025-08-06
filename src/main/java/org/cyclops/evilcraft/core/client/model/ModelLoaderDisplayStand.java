package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.client.render.model.ModelDisplayStand;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom model loader for the display stand.
 * @author rubensworks
 */
public class ModelLoaderDisplayStand implements UnbakedModelLoader<ModelDisplayStand> {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "display_stand");

    private static final ModelLoaderDisplayStand INSTANCE = new ModelLoaderDisplayStand();

    private final Map<String, ResolvedModel> resolvedModels = new HashMap<>();
    private ModelBaker baker;
    private TextureSlots textureSlots;

    private ModelLoaderDisplayStand() {

    }

    public static ModelLoaderDisplayStand getInstance() {
        return INSTANCE;
    }

    public Map<String, ResolvedModel> getResolvedModels() {
        return resolvedModels;
    }

    public ModelBaker getBaker() {
        return baker;
    }

    public void setBaker(ModelBaker baker) {
        this.baker = baker;
    }

    public TextureSlots getTextureSlots() {
        return textureSlots;
    }

    public void setTextureSlots(TextureSlots textureSlots) {
        this.textureSlots = textureSlots;
    }

    @Override
    public ModelDisplayStand read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        modelContents.remove("loader");
        BlockModel modelBlock = deserializationContext.deserialize(modelContents, BlockModel.class);
        ModelDisplayStand model = new ModelDisplayStand(modelBlock);
        return model;
    }

}
