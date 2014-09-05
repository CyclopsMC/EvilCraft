package evilcraft.event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import evilcraft.client.KeyHandler;

/**
 * Event hook for {@link KeyInputEvent}
 * 
 * @author immortaleeb
 *
 */
public class KeyInputEventHook {
	private Map<KeyBinding, List<KeyHandler>> keyHandlerMap;
	
	private static KeyInputEventHook instance;
	
	/**
	 * Gets the unique instance (singleton) of this class.
	 * @return Singleton of this class.
	 */
	public static KeyInputEventHook getInstance() {
		if (instance == null)
			instance = new KeyInputEventHook();
		
		return instance;
	}
	
	private KeyInputEventHook() {
		keyHandlerMap = new HashMap<KeyBinding, List<KeyHandler>>();
	}
	
	/**
	 * Handles key presses for keybindings added by EvilCraft
	 * @param event
	 */
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onPlayerKeyInput(KeyInputEvent event) {
		for (KeyBinding kb : keyHandlerMap.keySet()) {
			if (kb.isPressed())
				fireKeyPressed(kb);
		}
	}
	
	private void fireKeyPressed(KeyBinding kb) {
		for (KeyHandler h : keyHandlerMap.get(kb)) {
			h.onKeyPressed(kb);
		}
	}
	
	/**
	 * Binds a {@link KeyHandler} to key presses of the
	 * specified {@link KeyBinding}. Whenever the {@link KeyBinding}
	 * is pressed, {@link KeyHandler#onKeyPressed(KeyBinding)} is called.
	 * 
	 * @param kb {@link KeyBinding} to which we bind the {@link KeyHandler}.
	 * @param handler {@link KeyHandler} that will be linked to presses of the given {@link KeyBinding}.
	 */
	public void addKeyHandler(KeyBinding kb, KeyHandler handler) {
		List<KeyHandler> handlers = keyHandlerMap.get(kb);
		
		if (handlers == null) {
			handlers = new LinkedList<KeyHandler>();
			keyHandlerMap.put(kb, handlers);
		}
		
		if (!handlers.contains(handler))
			handlers.add(handler);
	}
}
