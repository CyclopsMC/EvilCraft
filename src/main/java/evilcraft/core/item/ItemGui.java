package evilcraft.core.item;

import evilcraft.EvilCraft;
import evilcraft.Reference;
import evilcraft.client.gui.GuiHandler;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import evilcraft.core.helper.Helpers;
import evilcraft.core.helper.Helpers.IDType;
import evilcraft.core.inventory.IGuiContainerProvider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Configurable item that can show a GUI on right clicking.
 * @author rubensworks
 *
 */
public class ItemGui extends ConfigurableItem implements IGuiContainerProvider {

	private int guiID;
	
	private Class<? extends Container> container;
    @SideOnly(Side.CLIENT)
    private Class<? extends GuiScreen> gui;
	
	/**
     * Make a new item instance.
     * @param eConfig Config for this blockState.
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
    public void setGUI(Class<? extends GuiScreen> gui) {
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
    public Class<? extends GuiScreen> getGUI() {
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
    
    /**
     * Open the gui for a certain item index in the player inventory.
     * @param world The world.
     * @param player The player.
     * @param itemIndex The item index in the player inventory.
     */
    public void openGuiForItemIndex(World world, EntityPlayer player, int itemIndex) {
    	GuiHandler.setTemporaryItemIndex(itemIndex);
    	if(!world.isRemote || isClientSideOnlyGui()) {
    		player.openGui(EvilCraft._instance, getGuiID(), world, (int) player.posX, (int) player.posY, (int) player.posZ);
    	}
    }

    protected boolean isClientSideOnlyGui() {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
    	openGuiForItemIndex(world, player, player.inventory.currentItem);
    	return itemStack;
    }

}
