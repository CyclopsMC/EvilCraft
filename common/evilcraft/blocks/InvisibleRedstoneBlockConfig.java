package evilcraft.blocks;

import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

public class InvisibleRedstoneBlockConfig extends BlockConfig {
    public static InvisibleRedstoneBlockConfig _instance;

    public InvisibleRedstoneBlockConfig() {
        super(Reference.BLOCK_INVISIBLEREDSTONE, "Invisible Redstone Block",
                "invisibleRedstoneBlock", null, InvisibleRedstoneBlock.class);
    }
}
