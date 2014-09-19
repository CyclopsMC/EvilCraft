package evilcraft.core.item;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.core.config.configurable.ConfigurableItem;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.Helpers;
import evilcraft.core.helper.Helpers.IDType;
import evilcraft.core.inventory.IGuiContainerProvider;

/**
 * Configurable item that can show a GUI on right clicking.
 * @author rubensworks
 *
 */
public class ItemGui extends ConfigurableItem implements IGuiContainerProvider {

	private int guiID;
	
	private Class<? extends Container> container;
    @SideOnly(Side.CLIENT)
    private Class<? extends GuiContainer> gui;
	
	/**
     * Make a new item instance.
     * @param eConfig Config for this block.
     */
    @SuppressWarnings({ "rawtypes" })
	protected ItemGui(ExtendedConfig eConfig) {
		super(eConfig);
		this.guiID = Helpers.getNewId(IDType.GUI);
	}
    
    @Override
    public int getGuiID() {
        return this.guiID;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    public void setGUI(Class<? extends GuiContainer> gui) {
        this.gui = gui;
    }
    
    @Override
	public void setContainer(Class<? extends Container> container) {
        this.container = container;
    }
    
    @Override
	public Class<? extends Container> getContainer() {
        return container;
    }
    
    @Override
	@SideOnly(Side.CLIENT)
    public Class<? extends GuiContainer> getGUI() {
        return gui;
    }
    
    @Override
	public boolean onDroppedByPlayer(ItemStack itemstack, EntityPlayer player) {
		if(itemstack != null
				&& player instanceof EntityPlayerMP
				&& player.openContainer != null
				&& player.openContainer.getClass() == getContainer()) {
			((EntityPlayerMP) player).closeScreen();
		}
		return super.onDroppedByPlayer(itemstack, player);
	}
    
    @Override
    public String getGuiTexture() {
        return getGuiTexture("");
    }
    
    @Override
    public String getGuiTexture(String suffix) {
        return Reference.TEXTURE_PATH_GUI + eConfig.getNamedId() + "_gui" + suffix + ".png";
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    	player.openGui(EvilCraft._instance, getGuiID(), world, (int) player.posX, (int) player.posY, (int) player.posZ);
    	return itemStack;
    }

}
