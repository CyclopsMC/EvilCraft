package evilcraft.proxy;

import com.google.common.collect.Lists;
import evilcraft.EvilCraft;
import evilcraft.client.key.ExaltedCrafterKeyHandler;
import evilcraft.client.key.FartKeyHandler;
import evilcraft.client.key.Keys;
import evilcraft.event.PlayerTickEventHook;
import evilcraft.event.TextureStitchEventHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.client.key.KeyRegistry;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketHandler;
import org.cyclops.cyclopscore.proxy.ClientProxyComponent;
import org.lwjgl.input.Keyboard;

import java.util.List;

/**
 * Proxy for the client side.
 * 
 * @author rubensworks
 * 
 */
public class ClientProxy extends ClientProxyComponent {

	private final CommonProxy commonProxy = new CommonProxy();

	@Override
	protected ModBase getMod() {
		return EvilCraft._instance;
	}

	@Override
	public void registerKeyBindings(IKeyRegistry keyRegistry) {
		GameSettings settings = Minecraft.getMinecraft().gameSettings;

		for (KeyBinding key : Keys.KEYS)
			ClientRegistry.registerKeyBinding(key);

		// Fart key
        FartKeyHandler fartKeyHandler = new FartKeyHandler();

        keyRegistry.addKeyHandler(Keys.FART, fartKeyHandler);
        keyRegistry.addKeyHandler(Keys.EXALTEDCRAFTING, new ExaltedCrafterKeyHandler());
        keyRegistry.addKeyHandler(settings.keyBindSneak, fartKeyHandler);

		EvilCraft.clog("Registered key bindings");
	}

	@Override
	public void registerPacketHandlers(PacketHandler packetHandler) {
		super.registerPacketHandlers(packetHandler);
        commonProxy.registerPacketHandlers(packetHandler);
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

		FMLCommonHandler.instance().bus().register(new PlayerTickEventHook());
	}
    
}
