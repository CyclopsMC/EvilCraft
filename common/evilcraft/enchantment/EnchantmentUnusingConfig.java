package evilcraft.enchantment;

import net.minecraft.tileentity.TileEntity;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.entities.tileentities.TileBloodInfuser;

public class EnchantmentUnusingConfig extends ExtendedConfig {
    
    public static EnchantmentUnusingConfig _instance;

    public EnchantmentUnusingConfig() {
        super(
            0,// Not needed
            "Unusing",
            "unusing",
            null,
            EnchantmentUnusing.class
        );
    }
    
}
