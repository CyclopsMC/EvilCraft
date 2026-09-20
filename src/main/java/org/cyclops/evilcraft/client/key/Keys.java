package org.cyclops.evilcraft.client.key;

import com.google.common.collect.Lists;
import net.minecraft.client.KeyMapping;
import org.cyclops.cyclopscore.client.key.KeyRegistry;
import org.cyclops.evilcraft.EvilCraft;
import com.mojang.blaze3d.platform.InputConstants;

import java.util.List;

/**
 * Key bindings
 * @author rubensworks
 */
public final class Keys {

    /**
     * Fart key.
     */
    public static final KeyMapping FART = KeyRegistry.newKeyMapping(EvilCraft._instance, "fart", InputConstants.KEY_P);
    /**
     * Exalted Crafting key.
     */
    public static final KeyMapping EXALTEDCRAFTING = KeyRegistry.newKeyMapping(EvilCraft._instance, "exaltedCrafting", InputConstants.KEY_C);

    public static final List<KeyMapping> KEYS = Lists.newLinkedList();
    static {
        KEYS.add(FART);
        KEYS.add(EXALTEDCRAFTING);
    }

}
