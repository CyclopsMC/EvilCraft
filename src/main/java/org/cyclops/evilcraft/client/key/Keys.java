package org.cyclops.evilcraft.client.key;

import com.google.common.collect.Lists;
import net.minecraft.client.settings.KeyBinding;
import org.cyclops.cyclopscore.client.key.KeyRegistry;
import org.cyclops.evilcraft.EvilCraft;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * Key bindings
 * @author rubensworks
 */
public final class Keys {

    /**
     * Fart key.
     */
    public static final KeyBinding FART = KeyRegistry.newKeyBinding(EvilCraft._instance, "fart", GLFW.GLFW_KEY_P);
    /**
     * Exalted Crafting key.
     */
    public static final KeyBinding EXALTEDCRAFTING = KeyRegistry.newKeyBinding(EvilCraft._instance, "exaltedCrafting", GLFW.GLFW_KEY_C);

    public static final List<KeyBinding> KEYS = Lists.newLinkedList();
    static {
        KEYS.add(FART);
        KEYS.add(EXALTEDCRAFTING);
    }

}
