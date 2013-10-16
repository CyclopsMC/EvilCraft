package evilcraft.events;

import evilcraft.EvilCraft;
import evilcraft.items.BloodExtractor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fluids.FluidStack;

public class BloodObtainLivingDeathEventHook {

    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void LivingDeath(LivingDeathEvent event) {
        Entity e = event.source.getEntity();
        if(e != null && e instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) e;
            
            ItemStack[] hotbar = player.inventory.mainInventory;
            int i = 0;
            boolean filled = false;
            while(i < hotbar.length && !filled) {
                ItemStack itemStack = hotbar[i];
                if(itemStack != null && itemStack.getItem() == BloodExtractor.getInstance()) {
                    EvilCraft.log("Found BE!");
                    //FluidStack fluidStack = (FluidStack) itemStack;
                    //itemStack.getItem().setDamage(itemStack, 3);
                    filled = true;
                }
                i++;
            }
        }
        
        /*ItemStack result = attemptFill(event.world, event.target);
        if (result != null) {
            event.result = result;
            event.setResult(Result.ALLOW);
        }*/
    }
    
}
