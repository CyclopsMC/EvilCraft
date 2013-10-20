package evilcraft.blocks;

import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.ModLoader;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ConfigurableProperty;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.entities.monster.Werewolf;

public class DarkOreGlowingConfig extends ExtendedConfig {
    
    public static DarkOreGlowingConfig _instance;
    
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_OREGENERATION, comment = "How much ores per vein.")
    public static int blocksPerVein = 4;
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_OREGENERATION, comment = "How many veins per chunk.")
    public static int veinsPerChunk = 10;
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_OREGENERATION, comment = "Generation starts from this level.")
    public static int startY = 6;
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_OREGENERATION, comment = "Generation ends of this level.")
    public static int endY = 66;

    public DarkOreGlowingConfig() {
        super(
            3844,
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
