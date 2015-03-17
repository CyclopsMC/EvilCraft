package evilcraft.event;

import evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocksExtended;
import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
        if(!event.world.isRemote) {
            Block block = event.world.getBlockState(event.pos).getBlock();
            if(block instanceof ConfigurableBlockWithInnerBlocksExtended) {
                ((ConfigurableBlockWithInnerBlocksExtended) block).unwrapInnerBlock(event.world, event.pos);
                event.setResult(Result.ALLOW);
            }
        }
    }
    
}
