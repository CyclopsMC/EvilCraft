package org.cyclops.evilcraft.modcompat.baubles;

import org.cyclops.cyclopscore.modcompat.ICompatInitializer;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Mod compat for the Baubles mod.
 * @author rubensworks
 *
 */
public class BaublesModCompat implements IModCompat {

    private static boolean canBeUsed = false;

    @Override
    public String getId() {
        return Reference.MOD_BAUBLES;
    }

    @Override
    public boolean isEnabledDefault() {
        return true;
    }

    @Override
    public String getComment() {
        return "Vengeance Ring, Invigorating Pendant and Effortless Ring baubles.";
    }

    @Override
    public ICompatInitializer createInitializer() {
        return () -> canBeUsed = EvilCraft._instance.getModCompatLoader().shouldLoadModCompat(this);
    }

    /**
     * @return If the baubles mod compat can be used.
     */
    public static boolean canUse() {
        return canBeUsed;
    }

}
