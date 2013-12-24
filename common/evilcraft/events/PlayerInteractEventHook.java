package evilcraft.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import evilcraft.enchantment.EnchantmentUnusingConfig;

public class PlayerInteractEventHook {
    
    @ForgeSubscribe(priority = EventPriority.HIGH)
    public void PlayerInteract(PlayerInteractEvent event) {
        unusingEvent(event);
    }
    
    private void unusingEvent(PlayerInteractEvent event) {
        // WIP
        if(event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK)) {
            if(event.entityPlayer instanceof EntityPlayer) {
                ItemStack itemStack = event.entityPlayer.getCurrentEquippedItem();
                NBTTagList enchlist = itemStack.getEnchantmentTagList();
                for(int i = 0; i < enchlist.tagCount(); i++) {
                    if (((NBTTagCompound)enchlist.tagAt(i)).getShort("id") == EnchantmentUnusingConfig._instance.ID) {
                        int damageBorder = itemStack.getMaxDamage() - 1;
                        if(itemStack.getItemDamage() >= damageBorder) {
                            event.setCanceled(true);
                            itemStack.setItemDamage(damageBorder);
                            event.entityPlayer.stopUsingItem();
                        }
                    }
                }
            }
        }
    }
}
