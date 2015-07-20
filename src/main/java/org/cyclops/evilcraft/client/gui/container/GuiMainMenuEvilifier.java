package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.util.ResourceLocation;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.core.helper.obfuscation.ObfuscationHelpers;

import java.util.Random;

/**
 * This can add an evil surrounding to the main menu.
 * @author rubensworks
 *
 */
public class GuiMainMenuEvilifier {

    private static final int EVIL_MAINMENU_CHANCE = 2;

    /**
     * Make the main menu evil with a certain chance.
     */
    public static void evilifyMainMenu() {
        Random random = new Random();
        if(random.nextInt(EVIL_MAINMENU_CHANCE) == 0) {
            ResourceLocation[] evilTitlePanoramaPaths = new ResourceLocation[6];
            for(int i = 0; i < evilTitlePanoramaPaths.length; i++) {
                evilTitlePanoramaPaths[i] = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUIBACKGROUNDS + "evil_panorama_" + i + ".png");
            }
            ObfuscationHelpers.setTitlePanoramaPaths(evilTitlePanoramaPaths);
        }
    }

}
