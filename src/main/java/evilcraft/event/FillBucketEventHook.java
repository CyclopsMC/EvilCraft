package evilcraft.event;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.block.EternalWaterBlockConfig;
import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.FillBucketEvent;

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
        Block block = event.world.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ);
        if(block == EternalWaterBlockConfig._instance.getBlockInstance()) {
            event.setCanceled(true);
        }
    }
    
}
