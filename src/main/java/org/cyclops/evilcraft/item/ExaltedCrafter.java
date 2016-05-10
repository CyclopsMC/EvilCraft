package org.cyclops.evilcraft.item;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.cyclopscore.inventory.NBTSimpleInventoryItemHeld;
import org.cyclops.cyclopscore.item.ItemGui;
import org.cyclops.evilcraft.client.gui.container.GuiExaltedCrafter;
import org.cyclops.evilcraft.entity.item.EntityItemEmpowerable;
import org.cyclops.evilcraft.inventory.container.ContainerExaltedCrafter;

import java.util.List;

/**
 * A portable crafting table with a built-in ender chest.
 * @author rubensworks
 *
 */
public class ExaltedCrafter extends ItemGui implements IItemEmpowerable {

    private static final String NBT_RETURNTOINNER = "returnToInner";
    
    private static ExaltedCrafter _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static ExaltedCrafter getInstance() {
        return _instance;
    }

    public ExaltedCrafter(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
    	return isEmpowered(itemStack);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemStack){
        return isEmpowered(itemStack) ? EnumRarity.UNCOMMON : super.getRarity(itemStack);
    }
    
    @Override
	public boolean isEmpowered(ItemStack itemStack) {
    	return (itemStack.getItemDamage() >> 1 & 1) == 1;
    }
    
    @Override
	public ItemStack empower(ItemStack itemStack) {
        if(itemStack.getItem() == this) {
            itemStack.setItemDamage(itemStack.getItemDamage() | 2);
        }
        return itemStack;
    }
    
    public boolean isWooden(ItemStack itemStack) {
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
    		return new NBTSimpleInventoryItemHeld(player, itemIndex, 27, 64);
    	}
    	return player.getInventoryEnderChest();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);
        if(isEmpowered(itemStack))
            list.add(TextFormatting.RED + "Empowered");
    }
    
    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
    	return true;
    }
    
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemStack) {
    	return new EntityItemEmpowerable(world, (EntityItem) location);
    }

    public void setReturnToInner(ItemStack itemStack, boolean returnToInner) {
        if(itemStack.hasTagCompound()) {
            itemStack.getTagCompound().setBoolean(NBT_RETURNTOINNER, returnToInner);
        }
    }

    public boolean isReturnToInner(ItemStack itemStack) {
        if(itemStack.hasTagCompound()) {
            return itemStack.getTagCompound().getBoolean(NBT_RETURNTOINNER);
        }
        return false;
    }

    @Override
    public Class<? extends Container> getContainer() {
        return ContainerExaltedCrafter.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Class<? extends GuiScreen> getGui() {
        return GuiExaltedCrafter.class;
    }
}
