package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.evilcraft.tileentity.TileEternalWater;

/**
 * Block for {@link BlockEternalWaterConfig}.
 * @author rubensworks
 */
public class BlockEternalWater extends BlockTile {

    public BlockEternalWater(Block.Properties properties) {
        super(properties, TileEternalWater::new);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canBeReplacedByLogs(BlockState state, IWorldReader world, BlockPos pos) {
        return false;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos blockPos, PlayerEntity player,
                                             Hand hand, BlockRayTraceResult p_225533_6_) {
        ItemStack itemStack = player.inventory.getCurrentItem();
        if (!itemStack.isEmpty()) {
            if (itemStack.getItem() == Items.BUCKET) {
                if (!world.isRemote()) {
                    itemStack.shrink(1);
                    if (itemStack.isEmpty()) {
                        player.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
                    } else if (!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
                        player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                    }
                    world.playSound((PlayerEntity)null, blockPos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            } else {
                FluidUtil.getFluidHandler(itemStack)
                        .ifPresent(fluidHandler -> fluidHandler.fill(TileEternalWater.WATER, IFluidHandler.FluidAction.EXECUTE));
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void stopFillWithEternalWaterBlock(FillBucketEvent event) {
        if (event.getTarget() != null && event.getTarget().getType() == RayTraceResult.Type.BLOCK) {
            Block block = event.getWorld().getBlockState(((BlockRayTraceResult) event.getTarget()).getPos()).getBlock();
            if (block instanceof BlockEternalWater) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return Fluids.WATER.getDefaultState();
    }
}
