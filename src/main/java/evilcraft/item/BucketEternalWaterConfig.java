package evilcraft.item;

import evilcraft.core.config.configurable.ConfigurableItemBucket;
import evilcraft.core.config.configurable.IConfigurable;
import evilcraft.core.config.extendedconfig.ItemBucketConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Config for the Eternal Water Bucket.
 * @author rubensworks
 *
 */
public class BucketEternalWaterConfig extends ItemBucketConfig {

    /**
     * The unique instance.
     */
    public static BucketEternalWaterConfig _instance;

    /**
     * Make a new instance.
     */
    public BucketEternalWaterConfig() {
        super(
        	true,
            "bucketEternalWater",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        ConfigurableItemBucket bucket = new ConfigurableItemBucket(this, Blocks.flowing_water) {

            @Override
            public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
                MovingObjectPosition position = this.getMovingObjectPositionFromPlayer(world, player, true);
                if(position != null && position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    Block block = world.getBlockState(position.func_178782_a()).getBlock();
                    if(block == Blocks.water) {
                        world.setBlockToAir(position.func_178782_a());
                        return itemStack;
                    }
                }

                ItemStack result = super.onItemRightClick(itemStack, world, player);
                if(result != null && result.getItem() == Items.bucket) return new ItemStack(getContainerItem());

                return result;
            }

        };
        bucket.setContainerItem(bucket);
        bucket.setMaxStackSize(64);
        return bucket;
    }

    @Override
    public Fluid getFluidInstance() {
        return FluidRegistry.WATER;
    }

    @Override
    public Block getFluidBlockInstance() {
        return Blocks.water;
    }
    
}
