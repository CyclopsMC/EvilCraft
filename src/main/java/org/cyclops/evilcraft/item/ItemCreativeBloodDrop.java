package org.cyclops.evilcraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodStain;
import org.cyclops.evilcraft.client.particle.ParticleBloodSplash;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;

import java.util.List;

/**
 * Containers that holds an infinite amount of blood.
 * @author rubensworks
 *
 */
public class ItemCreativeBloodDrop extends ItemBloodContainer {

    public static final int MB_FILL_PERTICK = 1000;

    public ItemCreativeBloodDrop(Item.Properties properties) {
        super(properties, MB_FILL_PERTICK);
        setPlaceFluids(true);
    }

    @Override
    public boolean isFoil(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, context, list, flag);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
                getDescriptionId() + ".info.auto_supply");
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int par4, boolean par5) {
        updateAutoFill(itemStack, world, entity);
        super.inventoryTick(itemStack, world, entity, par4, par5);
    }

    /**
     * Run an auto-fill tick for filling currently held container items from this item.
     * @param itemStack The item stack to fill from.
     * @param world The world.
     * @param entity The entity that holds this item.
     */
    public static void updateAutoFill(ItemStack itemStack, Level world, Entity entity) {
        IFluidHandler source = FluidUtil.getFluidHandler(itemStack).orElse(null);
        if(source != null && entity instanceof Player && !world.isClientSide() && ItemHelpers.isActivated(itemStack)) {
            FluidStack tickFluid = source.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            if(tickFluid != null && tickFluid.getAmount() > 0) {
                Player player = (Player) entity;
                for(InteractionHand hand : InteractionHand.values()) {
                    ItemStack held = player.getItemInHand(hand);
                    IFluidHandler fluidHandler = FluidUtil.getFluidHandler(held).orElse(null);
                    if (!held.isEmpty() && held != itemStack && fluidHandler != null && player.getUseItemRemainingTicks() == 0) {
                        if (fluidHandler.fill(tickFluid, IFluidHandler.FluidAction.SIMULATE) > 0) {
                            int filled = fluidHandler.fill(new FluidStack(tickFluid.getFluid(), MB_FILL_PERTICK), IFluidHandler.FluidAction.EXECUTE);
                            source.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return 13;
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if (context.getPlayer().isCrouching()) {
            BlockPos pos = context.getClickedPos().offset(0, 1, 0);
            if (RegistryEntries.BLOCK_BLOOD_STAIN.get().defaultBlockState().canSurvive(context.getLevel(), pos)) {
                if (context.getLevel().isClientSide()) {
                    ParticleBloodSplash.spawnParticles(context.getLevel(), pos, 5, 1 + context.getLevel().random.nextInt(2));
                } else {
                    if (context.getLevel().isEmptyBlock(pos)) {
                        // Add new stain
                        context.getLevel().setBlockAndUpdate(pos, RegistryEntries.BLOCK_BLOOD_STAIN.get().defaultBlockState());
                    }
                    if (context.getLevel().getBlockState(pos).getBlock() == RegistryEntries.BLOCK_BLOOD_STAIN.get()) {
                        // Add blood to existing block
                        BlockEntityHelpers.get(context.getLevel(), pos, BlockEntityBloodStain.class)
                                .ifPresent(tile -> tile.addAmount(FluidHelpers.BUCKET_VOLUME));
                    }
                }
                return InteractionResult.PASS;
            }
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!player.isCrouching()) {
            return super.use(world, player, hand);
        } else {
            BlockHitResult target = (BlockHitResult) this.getPlayerPOVHitResult(world, player, ClipContext.Fluid.ANY);
            if(target == null || target.getType() == Type.MISS) {
                if(!world.isClientSide()) {
                    ItemHelpers.toggleActivation(itemStack);
                }
            }
        }
        return MinecraftHelpers.successAction(itemStack);
    }
}
