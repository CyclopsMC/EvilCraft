package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.config.configurable.ConfigurableItemBucket;
import org.cyclops.cyclopscore.config.configurable.IConfigurable;
import org.cyclops.cyclopscore.config.extendedconfig.ItemBucketConfig;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.evilcraft.EvilCraft;

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
                EvilCraft._instance,
        	true,
            "bucketEternalWater",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        ConfigurableItemBucket bucket = new ConfigurableItemBucket(this, Blocks.FLOWING_WATER, new FluidStack(getFluidInstance(), Fluid.BUCKET_VOLUME)) {

            @Override
            public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
                RayTraceResult position = this.rayTrace(world, player, true);
                if(position != null && position.typeOfHit == RayTraceResult.Type.BLOCK) {
                    Block block = world.getBlockState(position.getBlockPos()).getBlock();
                    if(block == Blocks.WATER) {
                        world.setBlockToAir(position.getBlockPos());
                        return MinecraftHelpers.successAction(itemStack);
                    }
                }

                ActionResult<ItemStack> result = super.onItemRightClick(itemStack, world, player, hand);
                if(result.getResult() != null && result.getResult().getItem() == Items.BUCKET) return MinecraftHelpers.successAction(new ItemStack(getContainerItem()));

                return result;
            }

            @Override
            public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
                                          float hitX, float hitY, float hitZ, EnumHand hand) {
                IFluidHandler handler = TileHelpers.getCapability(world, pos,
                        side, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
                if(handler != null && !world.isRemote) {
                    handler.fill(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), true);
                    return EnumActionResult.SUCCESS;
                }
                return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ, hand);
            }

            @Override
            public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
                return new BucketWrapper(stack) {
                    @Override
                    protected void setFluid(Fluid fluid) {
                        // Do nothing: we want to keep the item in-place
                    }
                };
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
        return Blocks.WATER;
    }
    
}
