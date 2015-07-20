package org.cyclops.evilcraft.core.client.model;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import org.cyclops.evilcraft.Reference;

/**
 * Custom model loader for the broom item.
 * @author rubensworks
 */
public class BroomPartLoader implements ICustomModelLoader {

    private static final String LOCATION = "models/item/broom";

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(Reference.MOD_ID)
               && modelLocation.getResourcePath().startsWith(LOCATION);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) {
        return new BroomModel();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}
