package evilcraft.enchantment;

import net.minecraft.tileentity.TileEntity;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.entities.tileentities.TileBloodInfuser;

public class EnchantmentLifeStealingConfig extends ExtendedConfig {
    
    public static EnchantmentLifeStealingConfig _instance;

    public EnchantmentLifeStealingConfig() {
        super(
            Reference.ENCHANTMENT_LIFESTEALING,
            "Life Stealing",
            "enchantmentLifestealing",
            null,
            EnchantmentLifeStealing.class
        );
    }
    
}
