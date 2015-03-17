package evilcraft.core.inventory;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Interface for object providing gui-containers.
 * @author rubensworks
 *
 */
public interface IGuiContainerProvider {

	/**
	 * Set the gui.
	 * @param gui The gui.
	 */
	@SideOnly(Side.CLIENT)
    public void setGUI(Class<? extends GuiScreen> gui);
	/**
	 * Set the container.
	 * @param container The container.
	 */
	public void setContainer(Class<? extends Container> container);
	/**
     * Get the unique ID for the GUI this blockState has.
     * @return the GUI ID.
     */
    public int getGuiID();
	/**
     * Get the container for this blockState.
     * @return The container class.
     */
    public Class<? extends Container> getContainer();
    /**
     * Get the GUI for this blockState.
     * @return The GUI class.
     */
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGUI();
	/**
     * Get the texture path of the GUI.
     * @return The path of the GUI for this blockState.
     */
    public String getGuiTexture();
    /**
     * Get the texture path of the GUI.
     * @param suffix Suffix to add to the path.
     * @return The path of the GUI for this blockState.
     */
    public String getGuiTexture(String suffix);
	
}
