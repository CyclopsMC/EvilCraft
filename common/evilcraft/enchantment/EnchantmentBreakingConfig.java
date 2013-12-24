package evilcraft.enchantment;

import net.minecraft.tileentity.TileEntity;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.entities.tileentities.TileBloodInfuser;

public class EnchantmentBreakingConfig extends ExtendedConfig {
    
    public static EnchantmentBreakingConfig _instance;

    public EnchantmentBreakingConfig() {
        super(
            101,
            "Breaking",
            "breaking",
            null,
            EnchantmentBreaking.class
        );
    }
    
}
