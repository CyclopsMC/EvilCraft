package org.cyclops.evilcraft.client.gui.container;

import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.evilcraft.Reference;

import java.util.Random;

/**
 * This can add an evil surrounding to the main menu.
 * @author rubensworks
 *
 */
public class ContainerScreenMainMenuEvilifier {

    private static final int EVIL_MAINMENU_CHANCE = 2;

    /**
     * Make the main menu evil with a certain chance.
     */
    public static void evilifyMainMenu() {
        Random random = new Random();
        if(random.nextInt(EVIL_MAINMENU_CHANCE) == 0) {
            TitleScreen.CUBE_MAP = new CubeMap(new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_GUIBACKGROUNDS + "evil_panorama"));
        }
    }

}
