package evilcraft.items;

import evilcraft.Reference;
import evilcraft.api.config.ItemConfig;

public class RedstoneGrenadeConfig extends ItemConfig {
    
    public static RedstoneGrenadeConfig _instance;
    
    public RedstoneGrenadeConfig() {
        super(
                Reference.ITEM_REDSTONEGRENADE,
                "Redstone Pearl",
                "redstoneGrenade",
                null,
                RedstoneGrenade.class
            );
    }
}
