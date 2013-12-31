package evilcraft.proxies;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import evilcraft.EvilCraft;
import evilcraft.api.render.MultiPassBlockRenderer;

public class ClientProxy extends CommonProxy{
    
    public static Map<Class<? extends Entity>, Render> ENTITY_RENDERERS = new HashMap<Class<? extends Entity>, Render>();
    public static List<ISimpleBlockRenderingHandler> BLOCK_RENDERERS = new LinkedList<ISimpleBlockRenderingHandler>();
    
    // Renderers required for the API
    static {
        BLOCK_RENDERERS.add(new MultiPassBlockRenderer());
    }
    
    @Override
    public void registerRenderers() {
        // Entity renderers
        for(Entry<Class<? extends Entity>, Render> entry: ENTITY_RENDERERS.entrySet()) {
            RenderingRegistry.registerEntityRenderingHandler(entry.getKey(), entry.getValue());
            EvilCraft.log("Registered "+entry.getKey()+" renderer");
        }
        
        // Block renderers
        for(ISimpleBlockRenderingHandler renderer : BLOCK_RENDERERS)
            RenderingRegistry.registerBlockHandler(renderer);
    }
}
