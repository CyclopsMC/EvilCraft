package evilcraft.enchantment;

import net.minecraft.tileentity.TileEntity;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.entities.tileentities.TileBloodInfuser;

public class EnchantmentBreakingConfig extends ExtendedConfig {
    
    public static EnchantmentBreakingConfig _instance;

    public EnchantmentBreakingConfig() {
        super(
            Reference.ENCHANTMENT_BREAKING,
            "Breaking",
            "enchantmentBreaking",
            null,
            EnchantmentBreaking.class
        );
    }
    
}
