package org.cyclops.evilcraft.event;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.evilcraft.block.BlockEternalWater;

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
        if (event.getTarget() != null && event.getTarget().getType() == RayTraceResult.Type.BLOCK) {
            Block block = event.getWorld().getBlockState(((BlockRayTraceResult) event.getTarget()).getPos()).getBlock();
            if (block instanceof BlockEternalWater) {
                event.setCanceled(true);
            }
        }
    }
    
}
