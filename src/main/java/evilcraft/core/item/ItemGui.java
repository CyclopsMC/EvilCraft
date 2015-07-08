package evilcraft.core.item;

import evilcraft.EvilCraft;
import org.cyclops.cyclopscore.client.gui.GuiHandler;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.Helpers.IDType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItem;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.inventory.IGuiContainerProvider;

/**
 * Configurable item that can show a GUI on right clicking.
 * @author rubensworks
 *
 */
public abstract class ItemGui extends ConfigurableItem implements IGuiContainerProvider {

	private int guiID;
	
	/**
     * Make a new item instance.
     * @param eConfig Config for this blockState.
     */
    @SuppressWarnings({ "rawtypes" })
	protected ItemGui(ExtendedConfig eConfig) {
		super(eConfig);
		this.guiID = Helpers.getNewId(EvilCraft._instance, IDType.GUI);
	}

    @Override
    public ModBase getMod() {
        return EvilCraft._instance;
    }
    
    @Override
    public int getGuiID() {
        return this.guiID;
    }
    
    @Override
	public abstract Class<? extends Container> getContainer();
    
    @Override
	@SideOnly(Side.CLIENT)
    public abstract Class<? extends GuiScreen> getGui();
    
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
    
    /**
     * Open the gui for a certain item index in the player inventory.
     * @param world The world.
     * @param player The player.
     * @param itemIndex The item index in the player inventory.
     */
    public void openGuiForItemIndex(World world, EntityPlayer player, int itemIndex) {
    	EvilCraft._instance.getGuiHandler().setTemporaryData(GuiHandler.GuiType.ITEM, itemIndex);
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
