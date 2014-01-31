package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;
import evilcraft.api.config.ElementTypeCategory;
import evilcraft.api.config.configurable.ConfigurableProperty;

public class NetherfishSpawnConfig extends BlockConfig {
    
    @ConfigurableProperty(category = ElementTypeCategory.OREGENERATION, comment = "How many veins per chunk.")
    public static int veinsPerChunk = 250;
    
    public static NetherfishSpawnConfig _instance;

    public NetherfishSpawnConfig() {
        super(
            Reference.BLOCK_DARKBLOCK,
            "Nether Monster Block",
            "netherMonsterBlock",
            null,
            NetherfishSpawn.class
        );
    }
    
    @Override
    public boolean hasSubTypes() {
        return true;
    }
    
}
