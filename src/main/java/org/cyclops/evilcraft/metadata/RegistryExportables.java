package org.cyclops.evilcraft.metadata;

import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.metadata.IRegistryExportableRegistry;

public class RegistryExportables {

    public static IRegistryExportableRegistry REGISTRY = CyclopsCoreNeoForge._instance.getRegistryManager()
            .getRegistry(IRegistryExportableRegistry.class);

    public static void load() {
        REGISTRY.register(new RegistryExportableBloodInfuserRecipe());
        REGISTRY.register(new RegistryExportableEnvirAccRecipe());
        REGISTRY.register(new RegistryExportableBroomModifier());
    }

}
