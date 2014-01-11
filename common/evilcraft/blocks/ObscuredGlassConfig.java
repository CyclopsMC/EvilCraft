package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class ObscuredGlassConfig extends BlockConfig {
    
    public static ObscuredGlassConfig _instance;

    public ObscuredGlassConfig() {
        super(
            Reference.BLOCK_OBSCUREDGLASS,
            "Obscured Glass",
            "obscuredGlass",
            null,
            ObscuredGlass.class
        );
    }
    
}
