package org.cyclops.evilcraft.event;

import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.evilcraft.block.EternalWaterBlockConfig;

/**
 * Event hook for {@link net.minecraftforge.event.entity.player.FillBucketEvent}.
 * @author rubensworks
 *
 */
public class FillBucketEventHook {

    /**
     * When a bonemeal event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onFillBucket(FillBucketEvent event) {
        stopFillWithEternalWaterBlock(event);
    }
    
    private void stopFillWithEternalWaterBlock(FillBucketEvent event) {
        if (event.getTarget() != null) {
            Block block = event.getWorld().getBlockState(event.getTarget().getBlockPos()).getBlock();
            if (block == EternalWaterBlockConfig._instance.getBlockInstance()) {
                event.setCanceled(true);
            }
        }
    }
    
}
