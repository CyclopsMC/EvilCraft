package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class NetherfishSpawnConfig extends BlockConfig {
    
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
