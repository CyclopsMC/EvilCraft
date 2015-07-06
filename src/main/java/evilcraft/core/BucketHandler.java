package evilcraft.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * This will take care of the logic of custom buckets, so they can be filled like other buckets.
 * @author rubensworks
 *
 */
public class BucketHandler {

    private static BucketHandler _instance;
    
    /**
     * The map that will map the fluid blockState to the respective bucket that is capable
     * to hold the fluid of that blockState.
     */
    public Map<Block, Item> buckets = new HashMap<Block, Item>();
    
    /**
     * Get the unique instance.
     * @return The unique instance.
     */
    public static BucketHandler getInstance() {
        if(_instance == null) _instance = new BucketHandler();
        return _instance;
    }

    private BucketHandler() {
        
    }

    /**
     * Called when player right clicks with an empty bucket on a fluid blockState.
     * @param event The Forge event required for this.
     */
    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event) {
        ItemStack result = fillCustomBucket(event.world, event.target, event.current);

        if (result != null) {
            event.result = result;
            event.setResult(Result.ALLOW);
        }
    }

    private ItemStack fillCustomBucket(World world, MovingObjectPosition pos, ItemStack current) {
        Block block = world.getBlockState(pos.getBlockPos()).getBlock();

        Item bucket = buckets.get(block);
        if (bucket != null && world.getBlockState(pos.getBlockPos()) == block.getDefaultState() &&
                ItemStack.areItemStacksEqual(current, bucket.getContainerItem(current))) {
            world.setBlockToAir(pos.getBlockPos());
            return new ItemStack(bucket);
        } else {
            return null;
        }
    }
}
