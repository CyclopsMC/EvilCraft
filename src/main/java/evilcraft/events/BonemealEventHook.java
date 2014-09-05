package evilcraft.events;

import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.BonemealEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import evilcraft.core.config.configurable.ConfigurableBlockSapling;

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
            Block block = event.world.getBlock(event.x, event.y, event.z);
            if(block instanceof ConfigurableBlockSapling) {
                ConfigurableBlockSapling sapling = (ConfigurableBlockSapling) block;
                sapling.func_149878_d(event.world, event.x, event.y, event.z, event.world.rand);
                event.setResult(Result.ALLOW);
            }
        }
    }
    
}
