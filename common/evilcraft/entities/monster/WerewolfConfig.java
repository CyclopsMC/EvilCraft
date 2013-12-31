package evilcraft.entities.monster;

import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.ModLoader;
import cpw.mods.fml.client.registry.RenderingRegistry;
import evilcraft.EvilCraft;
import evilcraft.api.config.ConfigHandler;
import evilcraft.api.config.ConfigurableProperty;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.proxies.ClientProxy;

public class WerewolfConfig extends ExtendedConfig {
    
    @ConfigurableProperty(category = ConfigHandler.CATEGORY_MOB, comment = "Should the Werewolf be enabled?")
    public static boolean isEnabled = true;
    
    public static WerewolfConfig _instance;

    public WerewolfConfig() {
        super(
            1,
            "Werewolf",
            "werewolf",
            null,
            Werewolf.class
        );
    }
    
    @Override
    public void onRegistered() {
        ClientProxy.ENTITY_RENDERERS.put(Werewolf.class, new RenderZombie());
        ModLoader.addSpawn(Werewolf.class, 1, 0, 1, EnumCreatureType.monster);
    }
    
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
    
}
