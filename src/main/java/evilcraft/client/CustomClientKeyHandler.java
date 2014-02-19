package evilcraft.client;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;
import evilcraft.Reference;

/**
 * Handles all custom key presses and saves their pressed state in a giant hashmap 
 * at client side (see CustomRemoteKeyHandler to save keypresses on the server side)
 * 
 * @author immortaleeb
 *
 */
public class CustomClientKeyHandler extends KeyHandler {
    
    /**
     * The key binding for toggling farts.
     */
	// TODO: make !configurable! and use StatCollector.translateToLocal(KEY_ID) and put in en_US.lang
    public static KeyBinding KEY_FART = new KeyBinding(Reference.KEY_FART, Keyboard.KEY_P);
    
    private Map<String, Boolean> keyMap = new HashMap<String, Boolean>();
    
    private static CustomClientKeyHandler _instance;
    
    /**
     * Get the unique instance.
     * @return The unique instance.
     */
    public static CustomClientKeyHandler getInstance() {
        if (_instance == null)
            _instance = new CustomClientKeyHandler();
        return _instance;
    }

    private CustomClientKeyHandler() {
        super(new KeyBinding[]{KEY_FART}, new boolean[]{false});
    }

    @Override
    public String getLabel() {
        return getClass().getSimpleName();
    }

    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
        if (tickEnd)
            setKeyPressed(kb);
    }

    @Override
    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
        if (tickEnd)
            setKeyPressed(kb);
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    private void setKeyPressed(KeyBinding kb) {
        keyMap.put(kb.keyDescription, kb.pressed);
    }
    
    /**
     * Check if the given key is pressed.
     * @param key The key to check.
     * @return If that key is pressed.
     */
    public static boolean isKeyPressed(String key) {
        Boolean b = getInstance().keyMap.get(key);
        return (b != null) ? b.booleanValue() : false;
    }
}
