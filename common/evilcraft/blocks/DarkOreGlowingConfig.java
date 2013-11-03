package evilcraft.blocks;

import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.ModLoader;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ConfigurableProperty;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.entities.monster.Werewolf;

public class DarkOreGlowingConfig extends ExtendedConfig {
    
    public static DarkOreGlowingConfig _instance;

    public DarkOreGlowingConfig() {
        super(
            Reference.BLOCK_DARKOREGLOWING,
            "Dark Ore",
            "darkoreglowing",
            null,
            DarkOre.class
        );
    }
    
    @Override
    public void onRegistered() {
         DarkOre darkOre = (DarkOre) this.getSubInstance();
         darkOre.enableGlowing();
    }
    
}
