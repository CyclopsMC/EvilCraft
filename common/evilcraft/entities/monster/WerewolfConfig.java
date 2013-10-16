package evilcraft.entities.monster;

import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.ModLoader;
import cpw.mods.fml.client.registry.RenderingRegistry;
import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;

public class WerewolfConfig extends ExtendedConfig {
    
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
        EvilCraft.renderers.put(Werewolf.class, new RenderZombie());
        ModLoader.addSpawn(Werewolf.class, 1, 0, 1, EnumCreatureType.monster);
    }
    
}
