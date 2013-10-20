package evilcraft.events;

import evilcraft.blocks.LiquidBlockBloodConfig;
import evilcraft.items.BucketBlood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.FillBucketEvent;

/**
 * Make sure that if we try to fill a bucket with blood, we get the correct bucket with blood.
 * @author Ruben Taelman
 *
 */
public class BloodBucketEventHook {
    
    @ForgeSubscribe(priority = EventPriority.NORMAL)
    public void FillBucket(FillBucketEvent event) {
        ItemStack result = attemptFill(event.world, event.target);
        if (result != null) {
            event.result = result;
            event.setResult(Result.ALLOW);
        }
    }

    private ItemStack attemptFill( World world, MovingObjectPosition p )
    {
        int id = world.getBlockId( p.blockX, p.blockY, p.blockZ );

        if ( id == LiquidBlockBloodConfig._instance.ID )
        {
            if ( world.getBlockMetadata( p.blockX, p.blockY, p.blockZ ) == 0 ) // Check that it is a source block
            {
                world.setBlock( p.blockX, p.blockY, p.blockZ, 0 ); // Remove the fluid block

                return new ItemStack( BucketBlood.getInstance() ); // Return the filled bucked item here.
            }
        }

        return null;
    }
}
