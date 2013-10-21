package evilcraft.events;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ItemFluidContainer;
import evilcraft.EvilCraft;
import evilcraft.api.HotbarIterator;
import evilcraft.items.BloodExtractor;
import evilcraft.liquids.Blood;

public class BloodObtainLivingDeathEventHook {

    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void LivingDeath(LivingDeathEvent event) {
        Entity e = event.source.getEntity();
        if(e != null && e instanceof EntityPlayerMP && event.entityLiving != null) {
            EntityPlayerMP player = (EntityPlayerMP) e;
           
            int health = Float.floatToIntBits(event.entityLiving.getMaxHealth());
            int toFill = health * 10 + (new Random()).nextInt(health * 90);
            
            HotbarIterator it = new HotbarIterator(player);
            while(it.hasNext() && toFill > 0) {
                ItemStack itemStack = it.next();
                if(itemStack != null && itemStack.getItem() == BloodExtractor.getInstance()) {
                    ItemFluidContainer container = (ItemFluidContainer) itemStack.getItem();
                    toFill -= container.fill(itemStack, new FluidStack(Blood.getInstance(), toFill), true);
                }
            }
        }
    }
    
}
