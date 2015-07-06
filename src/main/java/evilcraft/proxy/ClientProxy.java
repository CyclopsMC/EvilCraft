package evilcraft.proxy;

import evilcraft.EvilCraft;
import evilcraft.client.ExaltedCrafterKeyHandler;
import evilcraft.client.FartKeyHandler;
import evilcraft.client.KeyHandler;
import evilcraft.client.Keys;
import evilcraft.event.KeyInputEventHook;
import evilcraft.event.PlayerTickEventHook;
import evilcraft.event.TextureStitchEventHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

	@Override
	public void registerRenderers() {
		// Entity renderers
		for (Entry<Class<? extends Entity>, Render> entry : ENTITY_RENDERERS
				.entrySet()) {
			RenderingRegistry.registerEntityRenderingHandler(entry.getKey(),
					entry.getValue());
			EvilCraft.clog("Registered " + entry.getKey() + " renderer");
		}

		// Special TileEntity renderers
		for (Entry<Class<? extends TileEntity>, TileEntitySpecialRenderer> entry : TILE_ENTITY_RENDERERS
				.entrySet()) {
			ClientRegistry.bindTileEntitySpecialRenderer(entry.getKey(),
					entry.getValue());
			EvilCraft.clog("Registered " + entry.getKey() + " special renderer");
		}
	}

	@Override
	public void registerKeyBindings() {
		GameSettings settings = Minecraft.getMinecraft().gameSettings;

		for (Keys key : Keys.values())
			ClientRegistry.registerKeyBinding(key.keyBinding);

		// Fart key
		KeyHandler fartKeyHandler = new FartKeyHandler();

		Keys.FART.addKeyHandler(fartKeyHandler);
		Keys.EXALTEDCRAFTING.addKeyHandler(new ExaltedCrafterKeyHandler());
		KeyInputEventHook.getInstance().addKeyHandler(settings.keyBindSneak,
				fartKeyHandler);

		EvilCraft.clog("Registered key bindings");
	}

	@Override
	public void registerTickHandlers() {
		EvilCraft.clog("Registered tick handlers");
	}

	@Override
	public void registerEventHooks() {
		super.registerEventHooks();

		MinecraftForge.EVENT_BUS.register(new TextureStitchEventHook());

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
	    			.getSoundHandler().getSound(record.getSoundLocation()) == null) {
	    		playSoundMinecraft(x, y, z, sound, volume, frequency);
	    	} else {
		    	FMLClientHandler.instance().getClient().getSoundHandler()
		    		.playSound(record);
	    	}
    	}
    }
    
}
