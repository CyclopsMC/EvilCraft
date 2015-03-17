package evilcraft.event;

import evilcraft.block.EternalWaterBlockConfig;
import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
        Block block = event.world.getBlockState(event.target.func_178782_a()).getBlock();
        if(block == EternalWaterBlockConfig._instance.getBlockInstance()) {
            event.setCanceled(true);
        }
    }
    
}
