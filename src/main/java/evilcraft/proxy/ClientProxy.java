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
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Proxy for the client side.
 * 
 * @author rubensworks
 * 
 */
public class ClientProxy extends ClientProxyComponent {

	/**
	 * Map for {@link Entity} renderers.
	 */
	public static Map<Class<? extends Entity>, Render> ENTITY_RENDERERS = new HashMap<Class<? extends Entity>, Render>();
	/**
	 * Map for the {@link TileEntity} renderers.
	 */
	public static Map<Class<? extends TileEntity>, TileEntitySpecialRenderer> TILE_ENTITY_RENDERERS = new HashMap<Class<? extends TileEntity>, TileEntitySpecialRenderer>();

	private final CommonProxy commonProxy = new CommonProxy();

	@Override
	protected ModBase getMod() {
		return EvilCraft._instance;
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
	public void registerPacketHandlers() {
		super.registerPacketHandlers();
        commonProxy.registerPacketHandlers();
	}

    @Override
    public void registerTickHandlers() {
        super.registerTickHandlers();
        commonProxy.registerTickHandlers();
    }

	@Override
	public void registerEventHooks() {
		super.registerEventHooks();
        commonProxy.registerEventHooks();

		MinecraftForge.EVENT_BUS.register(new TextureStitchEventHook());

		FMLCommonHandler.instance().bus().register(KeyInputEventHook.getInstance());
		FMLCommonHandler.instance().bus().register(new PlayerTickEventHook());
	}
    
}
