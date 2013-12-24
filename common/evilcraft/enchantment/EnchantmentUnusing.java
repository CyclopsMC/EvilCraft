package evilcraft.enchantment;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraftforge.common.MinecraftForge;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigurableEnchantment;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.blocks.BloodyCobblestone;

public class EnchantmentUnusing extends ConfigurableEnchantment {
    
    private static EnchantmentUnusing _instance = null;
    
    public static void initInstance(ExtendedConfig eConfig) {
        if(_instance == null)
            _instance = new EnchantmentUnusing(eConfig);
        else EvilCraft.log("If you see this, something went horribly wrong while registring stuff!");
    }
    
    public static EnchantmentUnusing getInstance() {
        return _instance;
    }

    private EnchantmentUnusing(ExtendedConfig eConfig) {
        super(eConfig, 1, EnumEnchantmentType.all);
    }
    
    public int getMinEnchantability(int par1) {
        return 20;
    }
    
    public int getMaxEnchantability(int par1) {
        return 50;
    }
    
    public int getMaxLevel() {
        return 1;
    }
    
    

}
