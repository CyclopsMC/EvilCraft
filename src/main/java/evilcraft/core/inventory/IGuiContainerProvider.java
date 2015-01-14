package evilcraft.core.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;

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
     * Get the unique ID for the GUI this block has.
     * @return the GUI ID.
     */
    public int getGuiID();
	/**
     * Get the container for this block.
     * @return The container class.
     */
    public Class<? extends Container> getContainer();
    /**
     * Get the GUI for this block.
     * @return The GUI class.
     */
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGUI();
	/**
     * Get the texture path of the GUI.
     * @return The path of the GUI for this block.
     */
    public String getGuiTexture();
    /**
     * Get the texture path of the GUI.
     * @param suffix Suffix to add to the path.
     * @return The path of the GUI for this block.
     */
    public String getGuiTexture(String suffix);
	
}
