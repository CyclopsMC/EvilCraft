package evilcraft.item;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

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
import evilcraft.entity.item.EntityItemEmpowerable;
import evilcraft.inventory.container.ContainerExaltedCrafter;
import evilcraft.network.PacketHandler;
import evilcraft.network.packet.ExaltedCrafterOpenPacket;

/**
 * A portable crafting table with a built-in ender chest.
 * @author rubensworks
 *
 */
public class ExaltedCrafter extends ItemGui implements KeyHandler, IItemEmpowerable {
    
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
    public boolean hasEffect(ItemStack itemStack){
    	return isEmpowered(itemStack);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemStack){
        return isEmpowered(itemStack) ? EnumRarity.uncommon : super.getRarity(itemStack);
    }
    
    @Override
	public boolean isEmpowered(ItemStack itemStack) {
    	return (itemStack.getItemDamage() >> 1 & 1) == 1;
    }
    
    @Override
	public void empower(ItemStack itemStack) {
        if(itemStack.getItem() == this) {
            itemStack.setItemDamage(itemStack.getItemDamage() | 2);
        }
    }
    
    protected boolean isWooden(ItemStack itemStack) {
    	return (itemStack.getItemDamage() & 1) == 1;
    }
    
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return super.getUnlocalizedName() + (isWooden(itemStack) ? ".wood" : "");
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
    	itemList.add(new ItemStack(item, 1, 0));
    	itemList.add(new ItemStack(item, 1, 1));
    	itemList.add(new ItemStack(item, 1, 2));
    	itemList.add(new ItemStack(item, 1, 3));
    }
    
    /**
     * Get the supplementary inventory of the given crafter.
     * @param player The player using the crafter.
     * @param itemStack The item stack.
     * @param itemIndex The item index.
     * @return The inventory.
     */
    public IInventory getSupplementaryInventory(EntityPlayer player, ItemStack itemStack, int itemIndex) {
    	if(isWooden(itemStack)) {
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
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        if(isEmpowered(itemStack))
            list.add(EnumChatFormatting.RED + "Empowered");
    }
    
    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
    	return true;
    }
    
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemStack) {
    	return new EntityItemEmpowerable(world, location.posX, location.posY, location.posZ, ((EntityItem) location).getEntityItem());
    }

}
