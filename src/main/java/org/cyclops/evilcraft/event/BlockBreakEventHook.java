package org.cyclops.evilcraft.event;

import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocksExtended;

/**
 * Event hook for {@link net.minecraftforge.event.world.BlockEvent.BreakEvent}.
 * @author rubensworks
 *
 */
public class BlockBreakEventHook {

    /**
     * When an event is received.
     * @param event The received event.
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onEvent(BlockEvent.BreakEvent event) {
        unwrapInnerBlock(event);
    }
    
    private void unwrapInnerBlock(BlockEvent.BreakEvent event) {
        if(!event.getWorld().isRemote) {
            Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
            if(block instanceof ConfigurableBlockWithInnerBlocksExtended) {
                ((ConfigurableBlockWithInnerBlocksExtended) block).unwrapInnerBlock(event.getWorld(), event.getPos());
                event.setResult(Result.ALLOW);
            }
        }
    }
    
}
