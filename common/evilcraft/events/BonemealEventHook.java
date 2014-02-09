package evilcraft.events;

import net.minecraft.block.Block;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;
import evilcraft.api.config.configurable.ConfigurableBlockSapling;

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
    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void onBonemeal(BonemealEvent event) {
        bonemealCustomSaplings(event);
    }
    
    private void bonemealCustomSaplings(BonemealEvent event) {
        if(!event.world.isRemote) {
            Block block = Block.blocksList[event.world.getBlockId(event.X, event.Y, event.Z)];
            if(block instanceof ConfigurableBlockSapling) {
                ConfigurableBlockSapling sapling = (ConfigurableBlockSapling) block;
                sapling.growTree(event.world, event.X, event.Y, event.Z, event.world.rand);
                event.setResult(Result.ALLOW);
            }
        }
    }
    
}
