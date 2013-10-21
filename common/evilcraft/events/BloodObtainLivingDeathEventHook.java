package evilcraft.events;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import evilcraft.EvilCraft;
import evilcraft.items.BloodExtractor;
import evilcraft.liquids.Blood;

public class BloodObtainLivingDeathEventHook {

    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void LivingDeath(LivingDeathEvent event) {
        Entity e = event.source.getEntity();
        if(e != null && e instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) e;
            
            ItemStack[] hotbar = player.inventory.mainInventory;
            int toFill = 100 + (new Random()).nextInt(900);
            int i = 0;
            while(i < hotbar.length && toFill > 0) {
                ItemStack itemStack = hotbar[i];
                if(itemStack != null && itemStack.getItem() == BloodExtractor.getInstance()) {
                    EvilCraft.log("Found BE!");
                    //itemStack.getItem().setDamage(itemStack, 3);
                    ItemFluidContainer container = (ItemFluidContainer) itemStack.getItem();
                    toFill -= container.fill(itemStack, new FluidStack(Blood.getInstance(), toFill), true);
                    EvilCraft.log("Content: "+container.getFluid(itemStack).amount);
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
