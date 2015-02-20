package evilcraft.event;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.core.config.configurable.ConfigurableBlockWithInnerBlocksExtended;
import net.minecraft.block.Block;
import net.minecraftforge.event.world.BlockEvent;

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
            Block block = event.world.getBlock(event.x, event.y, event.z);
            if(block instanceof ConfigurableBlockWithInnerBlocksExtended) {
                ((ConfigurableBlockWithInnerBlocksExtended) block).unwrapInnerBlock(event.world, event.x, event.y, event.z);
                event.setResult(Result.ALLOW);
            }
        }
    }
    
}
