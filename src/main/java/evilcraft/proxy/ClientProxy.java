package evilcraft.proxy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import evilcraft.EvilCraft;
import evilcraft.client.FartKeyHandler;
import evilcraft.client.KeyHandler;
import evilcraft.client.Keys;
import evilcraft.core.render.MultiPassBlockRenderer;
import evilcraft.event.KeyInputEventHook;
import evilcraft.event.PlaySoundAtEntityEventHook;
import evilcraft.event.PlayerTickEventHook;
import evilcraft.event.TextureStitchEventHook;

/**
 * Proxy for the client side.
 * 
 * @author rubensworks
 * 
 */
public class ClientProxy extends CommonProxy {
	
	private static final String SOUND_NONE = "none";

	/**
	 * Map for {@link Entity} renderers.
	 */
	public static Map<Class<? extends Entity>, Render> ENTITY_RENDERERS = new HashMap<Class<? extends Entity>, Render>();
	/**
	 * Map for the {@link TileEntity} renderers.
	 */
	public static Map<Class<? extends TileEntity>, TileEntitySpecialRenderer> TILE_ENTITY_RENDERERS = new HashMap<Class<? extends TileEntity>, TileEntitySpecialRenderer>();
	/**
	 * List of {@link Block} rendereres.
	 */
	public static List<ISimpleBlockRenderingHandler> BLOCK_RENDERERS = new LinkedList<ISimpleBlockRenderingHandler>();
	/**
     * Map for the {@link Item} renderers.
     */
    public static Map<Item, IItemRenderer> ITEM_RENDERERS = new HashMap<Item, IItemRenderer>();
	
	// Renderers required for the API
	static {
		BLOCK_RENDERERS.add(new MultiPassBlockRenderer());
	}

	@Override
	public void registerRenderers() {
		// Entity renderers
		for (Entry<Class<? extends Entity>, Render> entry : ENTITY_RENDERERS
				.entrySet()) {
			RenderingRegistry.registerEntityRenderingHandler(entry.getKey(),
					entry.getValue());
			EvilCraft.log("Registered " + entry.getKey() + " renderer");
		}

		// Special TileEntity renderers
		for (Entry<Class<? extends TileEntity>, TileEntitySpecialRenderer> entry : TILE_ENTITY_RENDERERS
				.entrySet()) {
			ClientRegistry.bindTileEntitySpecialRenderer(entry.getKey(),
					entry.getValue());
			EvilCraft.log("Registered " + entry.getKey() + " special renderer");
		}

		// Block renderers
		for (ISimpleBlockRenderingHandler renderer : BLOCK_RENDERERS)
			RenderingRegistry.registerBlockHandler(renderer);

		// Item renderers
        for(Entry<Item, IItemRenderer> entry : ITEM_RENDERERS.entrySet())
            MinecraftForgeClient.registerItemRenderer(entry.getKey(), entry.getValue());
	}

	@Override
	public void registerKeyBindings() {
		GameSettings settings = Minecraft.getMinecraft().gameSettings;

		for (Keys key : Keys.values())
			ClientRegistry.registerKeyBinding(key.keyBinding);

		// Fart key
		KeyHandler fartKeyHandler = new FartKeyHandler();

		Keys.FART.addKeyHandler(fartKeyHandler);
		KeyInputEventHook.getInstance().addKeyHandler(settings.keyBindSneak,
				fartKeyHandler);

		EvilCraft.log("Registered key bindings");
	}

	@Override
	public void registerTickHandlers() {
		EvilCraft.log("Registered tick handlers");
	}

	@Override
	public void registerEventHooks() {
		super.registerEventHooks();

		MinecraftForge.EVENT_BUS.register(new TextureStitchEventHook());
		MinecraftForge.EVENT_BUS.register(new PlaySoundAtEntityEventHook());

		FMLCommonHandler.instance().bus().register(KeyInputEventHook.getInstance());
		FMLCommonHandler.instance().bus().register(new PlayerTickEventHook());
	}
    
    @Override
    public void playSound(double x, double y, double z, String sound, float volume, float frequency,
    		String mod) {
    	if(!SOUND_NONE.equals(sound)) {
	    	ResourceLocation soundLocation = new ResourceLocation(mod, sound);
	    	PositionedSoundRecord record = new PositionedSoundRecord(soundLocation,
					volume, frequency, (float) x, (float) y, (float) z);
	    	
	    	// If we notice this sound is no mod sound, relay it to the default MC sound system.
	    	if(!mod.equals(DEFAULT_RESOURCELOCATION_MOD) && FMLClientHandler.instance().getClient()
	    			.getSoundHandler().getSound(record.getPositionedSoundLocation()) == null) {
	    		playSoundMinecraft(x, y, z, sound, volume, frequency);
	    	} else {
		    	FMLClientHandler.instance().getClient().getSoundHandler()
		    		.playSound(record);
	    	}
    	}
    }
    
}
