package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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
            "bucket_eternal_water",
            null,
            null
        );
    }

    @Override
    protected IConfigurable initSubInstance() {
        ConfigurableItemBucket bucket = new ConfigurableItemBucket(this, Blocks.FLOWING_WATER, new FluidStack(getFluidInstance(), Fluid.BUCKET_VOLUME)) {

            @Override
            public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
                ItemStack itemStack = player.getHeldItem(hand);
                RayTraceResult position = this.rayTrace(world, player, true);
                if(position != null && position.typeOfHit == RayTraceResult.Type.BLOCK) {
                    Block block = world.getBlockState(position.getBlockPos()).getBlock();
                    if(block == Blocks.WATER) {
                        world.setBlockToAir(position.getBlockPos());
                        return MinecraftHelpers.successAction(itemStack);
                    }
                }

                ActionResult<ItemStack> result = super.onItemRightClick(world, player, hand);
                if(result.getResult() != null && result.getResult().getItem() == Items.BUCKET) return MinecraftHelpers.successAction(new ItemStack(getContainerItem()));

                return result;
            }

            @Override
            public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side,
                                          float hitX, float hitY, float hitZ, EnumHand hand) {
                ItemStack stack = player.getHeldItem(hand);
                IFluidHandler handler = TileHelpers.getCapability(world, pos,
                        side, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
                if(handler != null && !world.isRemote) {
                    handler.fill(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), true);
                    return EnumActionResult.SUCCESS;
                }
                return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
            }

            @Override
            public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
                                          EnumFacing side, float hitX, float hitY, float hitZ) {
                IBlockState state = world.getBlockState(pos);
                Block block = state.getBlock();
                if(block instanceof BlockCauldron && !player.isSneaking()) {
                    if(!world.isRemote && state.getValue(BlockCauldron.LEVEL) < 3) {
                        player.addStat(StatList.CAULDRON_FILLED);
                        ((BlockCauldron)block).setWaterLevel(world, pos, state, 3);
                        world.playSound((EntityPlayer)null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                     }
                    return EnumActionResult.SUCCESS;
                }
                return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
            }

            @Override
            public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
                return new BucketWrapper(stack) {
                    @Override
                    protected void setFluid(FluidStack fluid) {
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
