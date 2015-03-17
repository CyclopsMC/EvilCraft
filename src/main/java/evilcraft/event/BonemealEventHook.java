package evilcraft.event;

import evilcraft.core.config.configurable.ConfigurableBlockSapling;
import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
        if(!event.world.isRemote) {
            Block block = event.world.getBlockState(event.pos).getBlock();
            if(block instanceof ConfigurableBlockSapling) {
                ConfigurableBlockSapling sapling = (ConfigurableBlockSapling) block;
                sapling.func_176478_d(event.world, event.pos, event.world.getBlockState(event.pos), event.world.rand);
                event.setResult(Result.ALLOW);
            }
        }
    }
    
}
