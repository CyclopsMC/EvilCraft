package org.cyclops.evilcraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

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
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        HitResult position = this.getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
        if(position != null && position.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = new BlockPos(position.getLocation());
            BlockState blockState = world.getBlockState(pos);
            if(blockState.getBlock() == Blocks.WATER && blockState.getValue(LiquidBlock.LEVEL) == 0) {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                return MinecraftHelpers.successAction(itemStack);
            }
        }

        InteractionResultHolder<ItemStack> result = super.use(world, player, hand);
        if(!result.getObject().isEmpty() && result.getObject().getItem() == Items.BUCKET) {
            return MinecraftHelpers.successAction(itemStack);
        }

        return result;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        IFluidHandler handler = BlockEntityHelpers.getCapability(context.getLevel(), context.getClickedPos(),
                context.getClickedFace(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).orElse(null);
        if(handler != null && !context.getLevel().isClientSide()) {
            handler.fill(new FluidStack(Fluids.WATER, FluidHelpers.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
            return InteractionResult.SUCCESS;
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        Block block = state.getBlock();

        if (!context.getPlayer().isCrouching()) {
            if (block == Blocks.WATER_CAULDRON || block == Blocks.CAULDRON) {
                if(!context.getLevel().isClientSide() && (block == Blocks.CAULDRON || state.getValue(LayeredCauldronBlock.LEVEL) < 3)) {
                    context.getPlayer().awardStat(Stats.USE_CAULDRON);
                    context.getLevel().setBlockAndUpdate(context.getClickedPos(), Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3));
                    context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(context);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
        return new FluidBucketWrapper(stack) {
            @Override
            protected void setFluid(FluidStack fluid) {
                // Do nothing: we want to keep the item in-place
            }
        };
    }
}
