package evilcraft.events;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import evilcraft.enchantment.EnchantmentBreaking;
import evilcraft.enchantment.EnchantmentBreakingConfig;
import evilcraft.enchantment.EnchantmentUnusingConfig;

public class PlayerInteractEventHook {
    
    @ForgeSubscribe(priority = EventPriority.HIGH)
    public void PlayerInteract(PlayerInteractEvent event) {
        unusingEvent(event);
        breakingEvent(event);
    }
    
    private void unusingEvent(PlayerInteractEvent event) {
        // WIP
        if(doesEnchantApply(event, EnchantmentUnusingConfig._instance.ID) > -1) {
            ItemStack itemStack = event.entityPlayer.getCurrentEquippedItem();
            int damageBorder = itemStack.getMaxDamage() - 1;
            if(itemStack.getItemDamage() >= damageBorder) {
                event.setCanceled(true);
                itemStack.setItemDamage(damageBorder);
                event.entityPlayer.stopUsingItem();
            }
        }
    }
    
    private void breakingEvent(PlayerInteractEvent event) {
        // WIP
        int i = doesEnchantApply(event, EnchantmentBreakingConfig._instance.ID);
        if(i > -1) {
            ItemStack itemStack = event.entityPlayer.getCurrentEquippedItem();
            NBTTagList enchlist = itemStack.getEnchantmentTagList();
            int level = ((NBTTagCompound)enchlist.tagAt(i)).getShort("level");
            int newDamage = itemStack.getItemDamage() + 2;
            if(EnchantmentBreaking.amplifyDamage(itemStack, level, new Random())
                    && newDamage <= itemStack.getMaxDamage()) {
                itemStack.setItemDamage(newDamage);
            }
        }
    }
    
    private int doesEnchantApply(PlayerInteractEvent event, int enchantID) {
        if(event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {
            if(event.entityPlayer instanceof EntityPlayer) {
                ItemStack itemStack = event.entityPlayer.getCurrentEquippedItem();
                NBTTagList enchlist = itemStack.getEnchantmentTagList();
                if(enchlist != null) {
                    for(int i = 0; i < enchlist.tagCount(); i++) {
                        if (((NBTTagCompound)enchlist.tagAt(i)).getShort("id") == enchantID) {
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }
}
