package org.cyclops.evilcraft.core.client.model;

import com.google.common.collect.Sets;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import org.cyclops.evilcraft.Reference;

import java.util.Set;

/**
 * Custom model loader for broom part items.
 * @author rubensworks
 */
public class BroomPartLoader implements ICustomModelLoader {

    public static final Set<ResourceLocation> LOCATIONS = Sets.newHashSet(
            new ResourceLocation(Reference.MOD_ID, "models/item/broom_part")
    );

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return LOCATIONS.contains(modelLocation);
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) {
        return new BroomPartModel();
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}
