package org.cyclops.evilcraft.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.blockentity.BlockEntityBloodStain;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.helper.ParticleHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

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

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(itemStack, context, tooltipDisplay, tooltipAdder, flag);
        IModHelpers.get().getL10NHelpers().addStatusInfo(tooltipAdder, ItemHelpers.isActivated(itemStack),
                getDescriptionId() + ".info.auto_supply");
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        updateAutoFill(itemStack, level, entity);
        super.inventoryTick(itemStack, level, entity, slot);
    }

    /**
     * Run an auto-fill tick for filling currently held container items from this item.
     * @param itemStack The item stack to fill from.
     * @param world The world.
     * @param entity The entity that holds this item.
     */
    public static void updateAutoFill(ItemStack itemStack, Level world, Entity entity) {
        ResourceHandler<FluidResource> source = itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack));
        if(source != null && entity instanceof Player && !world.isClientSide() && ItemHelpers.isActivated(itemStack)) {
            FluidStack tickFluid = FluidUtil.getStack(source, 0);
            if(tickFluid != null && tickFluid.getAmount() > 0) {
                Player player = (Player) entity;
                for(InteractionHand hand : InteractionHand.values()) {
                    ItemStack held = player.getItemInHand(hand);
                    ResourceHandler<FluidResource> fluidHandler = held.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forPlayerInteraction(player, hand));
                    if (!held.isEmpty() && held != itemStack && fluidHandler != null && player.getUseItemRemainingTicks() == 0) {
                        IModHelpersNeoForge.get().getFluidHelpers().move(source, fluidHandler, MB_FILL_PERTICK, null, false, false);
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
                    ParticleHelpers.spawnBloodSplashParticles(context.getLevel(), pos, 5, 1 + context.getLevel().getRandom().nextInt(2));
                } else {
                    if (context.getLevel().isEmptyBlock(pos)) {
                        // Add new stain
                        context.getLevel().setBlockAndUpdate(pos, RegistryEntries.BLOCK_BLOOD_STAIN.get().defaultBlockState());
                    }
                    if (context.getLevel().getBlockState(pos).getBlock() == RegistryEntries.BLOCK_BLOOD_STAIN.get()) {
                        // Add blood to existing block
                        IModHelpers.get().getBlockEntityHelpers().get(context.getLevel(), pos, BlockEntityBloodStain.class)
                                .ifPresent(tile -> tile.addAmount(IModHelpersNeoForge.get().getFluidHelpers().getBucketVolume(), true));
                    }
                }
                return InteractionResult.PASS;
            }
        }
        return super.onItemUseFirst(stack, context);
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
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
        return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
    }
}
