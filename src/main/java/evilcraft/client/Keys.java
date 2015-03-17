package evilcraft.client;

import evilcraft.core.helper.L10NHelpers;
import evilcraft.event.KeyInputEventHook;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * Enum that contains all custom hotkeys that
 * are added by EvilCraft. Every key also has a 
 * {@link KeyBinding} for that specific key.
 * 
 * @author immortaleeb
 *
 */
public enum Keys {
	
	/**
	 * Fart key.
	 */
	FART("fart", Keyboard.KEY_P),
	
	/**
	 * Exalted Crafting key.
	 */
	EXALTEDCRAFTING("exaltedCrafting", Keyboard.KEY_C);
	
	/**
	 * Prefix in the i18n file for keys.
	 */
	public static final String KEY_PREFIX = "key.";
	
	/**
	 * {@link KeyBinding} for the custom key.
	 */
	public KeyBinding keyBinding;
	
	private Keys(String id, int defaultKey) {
		this.keyBinding = new KeyBinding(
				t(id), 
				defaultKey, 
				t("categories.evilcraft"));
	}
	
	/**
	 * Short helper function for translating key descriptions
	 * and categories.
	 * 
	 * @param s Suffix of the id in the translation file.
	 * @return The translation for the given key description or category.
	 */
	private String t(String s) {
		return L10NHelpers.localize(KEY_PREFIX + s);
	}
	
	/**
	 * Binds a {@link KeyHandler} to the specific instance of the key.
	 * Whenever this key is pressed, {@link KeyHandler#onKeyPressed(KeyBinding)}
	 * will be called with the {@link KeyBinding} linked to this specific key.
	 *  
	 * @param handler {@link KeyHandler} that will handle presses of this specific key.
	 */
	public void addKeyHandler(KeyHandler handler) {
		KeyInputEventHook.getInstance().addKeyHandler(this.keyBinding, handler);
	}
	
}
