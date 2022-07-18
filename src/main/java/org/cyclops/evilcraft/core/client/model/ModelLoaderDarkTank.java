package org.cyclops.evilcraft.core.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.cyclops.evilcraft.client.render.model.ModelDarkTank;

/**
 * Custom model loader for the box of eternal closure.
 * @author rubensworks
 */
public class ModelLoaderDarkTank implements IGeometryLoader<ModelDarkTank> {

    @Override
    public ModelDarkTank read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
        modelContents.remove("loader");
        BlockModel modelBlock = deserializationContext.deserialize(modelContents, BlockModel.class);
        ModelDarkTank model = new ModelDarkTank(modelBlock);
        return model;
    }
}
