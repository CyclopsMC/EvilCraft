package evilcraft.api;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public interface IInformationProvider {
    
    public static String BLOCK_PREFIX = EnumChatFormatting.RED.toString();
    public static String ITEM_PREFIX = BLOCK_PREFIX;
    public static String INFO_PREFIX = EnumChatFormatting.BLUE.toString() + EnumChatFormatting.ITALIC.toString();
    
    public String getInfo(ItemStack itemStack);
    public void provideInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4);
}
