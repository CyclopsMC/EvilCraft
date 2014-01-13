package evilcraft.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public interface IInformationProvider {
    
    public static String BLOCK_PREFIX = EnumChatFormatting.RED.toString();
    public static String ITEM_PREFIX = BLOCK_PREFIX;
    
    public String getInfo(ItemStack itemStack);
}
