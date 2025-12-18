package org.cyclops.evilcraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;

/**
 * @author rubensworks
 */
public class ItemBucketEternalWater extends BucketItem {
    public ItemBucketEternalWater(Properties properties) {
        super(Fluids.WATER, properties);
    }

    @Override
    public ItemStack getCraftingRemainder(ItemStack itemStack) {
        return new ItemStack(this);
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand).copy();
        HitResult position = this.getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
        if(position != null && position.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = BlockPos.containing(position.getLocation());
            BlockState blockState = world.getBlockState(pos);
            if(blockState.getBlock() == Blocks.WATER && blockState.getValue(LiquidBlock.LEVEL) == 0) {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
            }
        }

        InteractionResult result = super.use(world, player, hand);
        if(result instanceof InteractionResult.Success success && !success.heldItemTransformedTo().isEmpty() && success.heldItemTransformedTo().getItem() == Items.BUCKET) {
            player.setItemInHand(hand, itemStack);
            return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
        }

        return result;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        ResourceHandler<FluidResource> handler = IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(context.getLevel(), context.getClickedPos(),
                context.getClickedFace(), Capabilities.Fluid.BLOCK).orElse(null);
        if(handler != null && !context.getLevel().isClientSide()) {
            try (var tx = Transaction.openRoot()) {
                handler.insert(FluidResource.of(Fluids.WATER), IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume(), tx);
                tx.commit();
            }
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
}
