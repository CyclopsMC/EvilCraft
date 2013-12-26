package evilcraft.entities.monster;

import net.minecraft.client.renderer.entity.RenderSilverfish;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.src.ModLoader;
import evilcraft.EvilCraft;
import evilcraft.api.config.ExtendedConfig;

public class NetherfishConfig extends ExtendedConfig {
    
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
    
}
