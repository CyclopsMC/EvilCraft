package evilcraft.proxies;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import com.jcraft.jorbis.Block;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import evilcraft.EvilCraft;
import evilcraft.api.render.MultiPassBlockRenderer;
import evilcraft.client.CustomClientKeyHandler;
import evilcraft.client.FartTickHandler;
import evilcraft.events.PlayerTickEventHook;

/**
 * Proxy for the client side.
 * @author rubensworks
 *
 */
public class ClientProxy extends CommonProxy{
    
    /**
     * Map for {@link Entity} renders.
     */
    public static Map<Class<? extends Entity>, Render> ENTITY_RENDERERS = new HashMap<Class<? extends Entity>, Render>();
    /**
     * Map for the {@link TileEntity} renderers.
     */
    public static Map<Class<? extends TileEntity>, TileEntitySpecialRenderer> TILE_ENTITY_RENDERERS = new HashMap<Class<? extends TileEntity>, TileEntitySpecialRenderer>();
    /**
     * List of {@link Block} renderers.
     */
    public static List<ISimpleBlockRenderingHandler> BLOCK_RENDERERS = new LinkedList<ISimpleBlockRenderingHandler>();
    /**
     * Map for the {@link Item} renderers.
     */
    public static Map<Integer, IItemRenderer> ITEM_RENDERERS = new HashMap<Integer, IItemRenderer>();
    
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
        
        // Special TileEntity renderers
        for(Entry<Class<? extends TileEntity>, TileEntitySpecialRenderer> entry: TILE_ENTITY_RENDERERS.entrySet()) {
            ClientRegistry.bindTileEntitySpecialRenderer(entry.getKey(), entry.getValue());
            EvilCraft.log("Registered "+entry.getKey()+" special renderer");
        }
        
        // Block renderers
        for(ISimpleBlockRenderingHandler renderer : BLOCK_RENDERERS)
            RenderingRegistry.registerBlockHandler(renderer);
        
        // Item renderers
        for(Entry<Integer, IItemRenderer> entry : ITEM_RENDERERS.entrySet())
            MinecraftForgeClient.registerItemRenderer(entry.getKey(), entry.getValue());
    }
    
    @Override
    public void registerKeyBindings() {
        //KeyBindingRegistry.registerKeyBinding(new CustomRemoteKeyHandler());
        
        // CustomClientKeyHandler
        KeyBindingRegistry.registerKeyBinding(CustomClientKeyHandler.getInstance());
        
        EvilCraft.log("Registered key bindings");
    }
    
    @Override
    public void registerTickHandlers() {
        TickRegistry.registerTickHandler(new FartTickHandler(), Side.CLIENT);
        TickRegistry.registerTickHandler(new PlayerTickEventHook(), Side.CLIENT);
        EvilCraft.log("Registered tick handlers");
    }
}
