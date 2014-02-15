package evilcraft.gui.client;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Random;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ResourceLocation;
import evilcraft.Reference;
import evilcraft.api.Helpers;

/**
 * This can add an evil surrounding to the main menu.
 * @author rubensworks
 *
 */
public class GuiMainMenuEvilifier {

    private static int EVIL_MAINMENU_CHANCE = 2;

    private static final String getPanoramaFieldName() {
        if(Helpers.isObfusicated()) {
            return "field_73978_o";
        } else {
            return "titlePanoramaPaths";
        }
    }

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

            try {
                Field titlePanoramaPathsField = GuiMainMenu.class.getDeclaredField(getPanoramaFieldName());

                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(titlePanoramaPathsField, titlePanoramaPathsField.getModifiers() & ~Modifier.FINAL);

                titlePanoramaPathsField.setAccessible(true);
                titlePanoramaPathsField.set(null, evilTitlePanoramaPaths);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
