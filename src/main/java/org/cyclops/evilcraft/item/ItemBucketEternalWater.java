package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;

import net.minecraft.item.Item.Properties;

/**
 * @author rubensworks
 */
public class ItemBucketEternalWater extends BucketItem {
    public ItemBucketEternalWater(Properties properties) {
        super(() -> Fluids.WATER, properties);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        RayTraceResult position = this.getPlayerPOVHitResult(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
        if(position != null && position.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos pos = new BlockPos(position.getLocation());
            BlockState blockState = world.getBlockState(pos);
            if(blockState.getBlock() == Blocks.WATER && blockState.getValue(FlowingFluidBlock.LEVEL) == 0) {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                return MinecraftHelpers.successAction(itemStack);
            }
        }

        ActionResult<ItemStack> result = super.use(world, player, hand);
        if(!result.getObject().isEmpty() && result.getObject().getItem() == Items.BUCKET) {
            return MinecraftHelpers.successAction(itemStack);
        }

        return result;
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        IFluidHandler handler = TileHelpers.getCapability(context.getLevel(), context.getClickedPos(),
                context.getClickedFace(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
        if(handler != null && !context.getLevel().isClientSide()) {
            handler.fill(new FluidStack(Fluids.WATER, FluidHelpers.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
            return ActionResultType.SUCCESS;
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        Block block = state.getBlock();
        if(block instanceof CauldronBlock && !context.getPlayer().isCrouching()) {
            if(!context.getLevel().isClientSide() && state.getValue(CauldronBlock.LEVEL) < 3) {
                context.getPlayer().awardStat(Stats.USE_CAULDRON);
                ((CauldronBlock)block).setWaterLevel(context.getLevel(), context.getClickedPos(), state, 3);
                context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            return ActionResultType.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
        return new FluidBucketWrapper(stack) {
            @Override
            protected void setFluid(FluidStack fluid) {
                // Do nothing: we want to keep the item in-place
            }
        };
    }
}
