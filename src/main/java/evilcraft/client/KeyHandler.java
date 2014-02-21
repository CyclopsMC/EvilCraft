package evilcraft.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.KeyBinding;

/**
 * A KeyHandler is responsible for handle key presses.
 * 
 * @author immortaleeb
 *
 */
@SideOnly(Side.CLIENT)
public interface KeyHandler {
	/**
	 * This method is called whenever a key, which is mapped
	 * to this KeyHandler is pressed.
	 * 
	 * @param kb {@link KeyBinding} of the key that was pressed.
	 */
	public void onKeyPressed(KeyBinding kb);
}
