package evilcraft.item;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.client.KeyHandler;
import evilcraft.client.Keys;
import evilcraft.client.gui.container.GuiExaltedCrafter;
import evilcraft.core.PlayerInventoryIterator;
import evilcraft.core.config.extendedconfig.ExtendedConfig;
import evilcraft.core.config.extendedconfig.ItemConfig;
import evilcraft.core.helper.MinecraftHelpers;
import evilcraft.core.inventory.NBTSimpleInventory;
import evilcraft.core.item.ItemGui;
import evilcraft.inventory.container.ContainerExaltedCrafter;
import evilcraft.network.PacketHandler;
import evilcraft.network.packet.ExaltedCrafterOpenPacket;

/**
 * A portable crafting table with a built-in ender chest.
 * @author rubensworks
 *
 */
public class ExaltedCrafter extends ItemGui implements KeyHandler {
    
    private static ExaltedCrafter _instance = null;
    
    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new ExaltedCrafter(eConfig);
        else
            eConfig.showDoubleInitError();
    }
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ExaltedCrafter getInstance() {
        return _instance;
    }

    private ExaltedCrafter(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);
        
        if (MinecraftHelpers.isClientSide())
            setGUI(GuiExaltedCrafter.class);
        
        setContainer(ContainerExaltedCrafter.class);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName() + ((itemStack.getItemDamage() == 1) ? ".wood" : "");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
    	itemList.add(new ItemStack(item));
    	itemList.add(new ItemStack(item, 1, 1));
    }
    
    /**
     * Get the supplementary inventory of the given crafter.
     * @param player The player using the crafter.
     * @param itemStack The item stack.
     * @param itemIndex The item index.
     * @return The inventory.
     */
    public IInventory getSupplementaryInventory(EntityPlayer player, ItemStack itemStack, int itemIndex) {
    	if(itemStack.getItemDamage() == 1) {
    		return new NBTSimpleInventory(player, itemIndex, 27, 64);
    	}
    	return player.getInventoryEnderChest();
    }
    
    @Override
	public void onKeyPressed(KeyBinding kb) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if(kb == Keys.EXALTEDCRAFTING.keyBinding) {
			Pair<Integer, ItemStack> found = null;
			PlayerInventoryIterator it = new PlayerInventoryIterator(player);
			while(it.hasNext() && found == null) {
				Pair<Integer, ItemStack> pair = it.nextIndexed();
				if(pair.getRight() != null && pair.getRight().getItem() == this) {
					found = pair;
				}
			}
			if(found != null) {
				openGuiForItemIndex(Minecraft.getMinecraft().theWorld, player, found.getLeft());
				PacketHandler.sendToServer(new ExaltedCrafterOpenPacket(found.getLeft()));
			}
		}
	}

}
