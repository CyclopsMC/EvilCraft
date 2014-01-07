package evilcraft.client;

import java.util.EnumSet;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import evilcraft.Reference;
import evilcraft.network.RemoteKey;

/**
 * This class handles custom hotkeys on the client side that need to be sent to the server.
 * Whenever a user presses one of the hotkeys, a RemoteKey is sent to the server where the
 * RemoteKeyHandler will process the remote key press.
 * 
 * Based on an elegant way to handle custom hotkeys server-side by
 * Nico Bergemann (BarracudaATA) in his "Dragon mounts" mod 
 * (http://www.minecraftforum.net/topic/827863-16x-dragon-mounts-r33-wip/)
 * 
 * @author immortaleeb
 *
 */

public class CustomKeyHandler extends KeyHandler {
	// Example keys:
	public static KeyBinding KEY_UP = new KeyBinding("key.evilcraft.broom.up", Keyboard.KEY_K);
	public static KeyBinding KEY_DOWN = new KeyBinding("key.evilcraft.broom.down", Keyboard.KEY_L);

	public CustomKeyHandler() {
		super(new KeyBinding[] { KEY_UP, KEY_DOWN }, new boolean[] {false, false});
	}

	@Override
	public String getLabel() {
		return getClass().getSimpleName();
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (tickEnd)
			sendPacket(kb);
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		if (tickEnd)
			sendPacket(kb);
	}
	
	private void sendPacket(KeyBinding kb) {
		sendPacket(kb, kb.pressed);
	}
	
	private void sendPacket(KeyBinding kb, boolean pressed) {
		// don't send packets if the GUI is active
		//if (Minecraft.getMinecraft().currentScreen != null)
		//	return;
		
		RemoteKey rk = new RemoteKey(kb);
		byte[] data = rk.toByteArray();
		
		if (data != null)
			PacketDispatcher.sendPacketToServer(PacketDispatcher.getPacket(Reference.MOD_CHANNEL, data));
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
