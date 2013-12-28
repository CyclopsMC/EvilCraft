package evilcraft.entities.monster;

import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.ModLoader;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ConfigurableProperty;
import evilcraft.api.config.ExtendedConfig;

public class NetherfishConfig extends ExtendedConfig {
    
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_MOB, comment = "Should the Netherfish be enabled?")
    public static boolean isEnabled = true;
    
    public static NetherfishConfig _instance;

    public NetherfishConfig() {
        super(
            1,
            "Netherfish",
            "netherfish",
            null,
            Netherfish.class
        );
    }
    
    @Override
    public void onRegistered() {
        EvilCraft.renderers.put(Netherfish.class, new RenderSilverfish());
        ModLoader.addSpawn(Netherfish.class, 1, 0, 1, EnumCreatureType.monster);
    }
    
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    
}
