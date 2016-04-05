package org.cyclops.evilcraft.event;

import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockSapling;

/**
 * Event hook for {@link BonemealEvent}.
 * @author rubensworks
 *
 */
public class BonemealEventHook {

    /**
     * When a bonemeal event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onBonemeal(BonemealEvent event) {
        bonemealCustomSaplings(event);
    }
    
    private void bonemealCustomSaplings(BonemealEvent event) {
        if(!event.getWorld().isRemote) {
            Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
            if(block instanceof ConfigurableBlockSapling) {
                ConfigurableBlockSapling sapling = (ConfigurableBlockSapling) block;
                sapling.grow(event.getWorld(), event.getPos(), event.getWorld().getBlockState(event.getPos()), event.getWorld().rand);
                event.setResult(Result.ALLOW);
            }
        }
    }
    
}
