package org.cyclops.evilcraft.core.helper.obfuscation;

import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Helper for getting private fields or methods.
 * @author rubensworks
 *
 */
public class ObfuscationHelpers {
	
	/**
	 * Set the private static 'PANORAMA_RESOURCES' field from @link{v}
	 * @param titlePanoramaPaths The panorama path.
	 */
	public static void setPanoramaResources(CubeMap titlePanoramaPaths) {
		Field field = ObfuscationReflectionHelper.findField(TitleScreen.class, "CUBE_MAP");
		
		Field modifiersField;
		try {
			modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
	        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

	        field.setAccessible(true);
	        field.set(null, titlePanoramaPaths);
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
