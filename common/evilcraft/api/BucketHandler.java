package evilcraft.api;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class BucketHandler {

    private static BucketHandler _instance;
    public Map<Block, Item> buckets = new HashMap<Block, Item>();
    
    public static BucketHandler getInstance() {
        if(_instance == null) _instance = new BucketHandler();
        return _instance;
    }

    private BucketHandler() {
        
    }

    @ForgeSubscribe
    public void onBucketFill(FillBucketEvent event) {
        ItemStack result = fillCustomBucket(event.world, event.target);

        if (result != null) {
            event.result = result;
            event.setResult(Result.ALLOW);
        }
    }

    private ItemStack fillCustomBucket(World world, MovingObjectPosition pos) {
        int blockID = world.getBlockId(pos.blockX, pos.blockY, pos.blockZ);

        Item bucket = buckets.get(Block.blocksList[blockID]);
        if (bucket != null && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0) {
            world.setBlock(pos.blockX, pos.blockY, pos.blockZ, 0);
            return new ItemStack(bucket);
        } else {
            return null;
        }
    }
}
