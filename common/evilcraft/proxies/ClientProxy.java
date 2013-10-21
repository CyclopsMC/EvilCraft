package evilcraft.proxies;

import java.util.Map.Entry;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import cpw.mods.fml.client.registry.RenderingRegistry;
import evilcraft.EvilCraft;

public class ClientProxy extends CommonProxy{
    @Override
    public void registerRenderers() {
        for(Entry<Class<? extends Entity>, Render> entry: EvilCraft.renderers.entrySet()) {
            RenderingRegistry.registerEntityRenderingHandler(entry.getKey(), entry.getValue());
            EvilCraft.log("Registered "+entry.getKey()+" renderer");
        }
    }
}
