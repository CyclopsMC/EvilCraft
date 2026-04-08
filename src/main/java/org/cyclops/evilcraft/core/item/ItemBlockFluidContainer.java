package org.cyclops.evilcraft.core.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.cyclops.cyclopscore.capability.fluid.FluidHandlerItemCapacity;
import org.cyclops.cyclopscore.capability.fluid.IFluidHandlerCapacity;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.item.ItemBlockNBT;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.block.BlockDarkTankConfig;
import org.cyclops.evilcraft.core.block.IBlockTank;
import org.cyclops.evilcraft.core.helper.BlockTankHelpers;
import org.cyclops.evilcraft.core.helper.ItemHelpers;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * {@link BlockItem} that can be used for blocks that have a tile entity with a fluid container.
 * The blockState must implement {@link IBlockTank}.
 * Instances of this will also keep it's tank capacity next to the contents.
 * @author rubensworks
 *
 */
public class ItemBlockFluidContainer extends ItemBlockNBT {

    private IBlockTank block;

    public ItemBlockFluidContainer(Block block, Properties builder) {
        super(block, builder);
        // Will crash if no valid instance of.
        this.block = (IBlockTank) block;

        EvilCraft._instance.getModEventBus().addListener(this::registerCapability);
    }

    private void registerCapability(RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.Fluid.ITEM, (stack, ctx) -> new FluidHandlerItemCapacity(ctx, block.getDefaultCapacity()), this);
        event.registerItem(org.cyclops.cyclopscore.Capabilities.Item.FLUID_HANDLER_CAPACITY, (stack, ctx) -> new FluidHandlerItemCapacity(ctx, block.getDefaultCapacity()), this);
    }

    @Override
    public boolean isFoil(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }

    @Override
    protected boolean itemStackDataToTile(ItemStack itemStack, BlockEntity tile) {
        IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(tile.getLevel(), tile.getBlockPos(), net.neoforged.neoforge.capabilities.Capabilities.Fluid.BLOCK)
                .ifPresent(fluidHandlerTile -> {
                    Optional.ofNullable(itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack)))
                            .ifPresent(fluidHandlerItem -> {
                                if (!fluidHandlerItem.getResource(0).isEmpty()) {
                                    try (var tx = Transaction.openRoot()) {
                                        fluidHandlerTile.insert(fluidHandlerItem.getResource(0), fluidHandlerItem.getAmountAsInt(0), tx);
                                        tx.commit();
                                    }
                                }
                                if (fluidHandlerTile instanceof IFluidHandlerCapacity) {
                                    try (var tx = Transaction.openRoot()) {
                                        ((IFluidHandlerCapacity) fluidHandlerTile).setTankCapacity(0, fluidHandlerItem.getCapacityAsInt(0, FluidResource.EMPTY), tx);
                                        tx.commit();
                                    }
                                }
                            });
                });
        return true;
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        if(block.isActivatable()) {
            return InteractionResult.SUCCESS.heldItemTransformedTo(block.toggleActivation(player.getItemInHand(hand), world, player));
        }
        return super.use(world, player, hand);
    }

    protected void autofill(@Nullable EquipmentSlot itemSlot, ResourceHandler<FluidResource> source, ItemStack sourceItem, Level world, Entity entity) {
        ItemHelpers.updateAutoFill(source, sourceItem, world, entity, BlockDarkTankConfig.autoFillBuckets);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        if(block.isActivatable() && block.isActivated(stack, Item.TooltipContext.of(level))) {
            autofill(slot, stack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(stack)), stack, level, entity);
        }
        super.inventoryTick(stack, level, entity, slot);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(itemStack, context, tooltipDisplay, tooltipAdder, flag);
        tooltipAdder.accept(BlockTankHelpers.getInfoTank(itemStack));
        if(block.isActivatable()) {
            IModHelpers.get().getL10NHelpers().addStatusInfo(tooltipAdder, block.isActivated(itemStack, context),
                    getDescriptionId() + ".info.auto_supply");
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem() || slotChanged;
    }
}
