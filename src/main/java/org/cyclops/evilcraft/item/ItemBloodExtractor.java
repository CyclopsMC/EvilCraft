package org.cyclops.evilcraft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.inventory.InventoryLocationPlayer;
import org.cyclops.cyclopscore.inventory.ItemAccessItemLocation;
import org.cyclops.cyclopscore.inventory.ItemLocation;
import org.cyclops.cyclopscore.inventory.PlayerInventoryIterator;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodStain;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.helper.ParticleHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Can extract blood from attacking mobs and {@link BlockBloodStain}.
 * @author rubensworks
 *
 */
public class ItemBloodExtractor extends ItemBloodContainer {

    public ItemBloodExtractor(Item.Properties properties) {
        super(properties, ItemBloodExtractorConfig.containerSize);
        setPlaceFluids(true);
        NeoForge.EVENT_BUS.addListener(this::bloodObtainEvent);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
        Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
        if(context.getPlayer().isCrouching()) {
            if(block instanceof BlockBloodStain) {
                RandomSource random = context.getLevel().getRandom();

                // Fill the extractor a bit
                return IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(context.getLevel(), context.getClickedPos(), net.neoforged.neoforge.capabilities.Capabilities.Fluid.BLOCK)
                        .<InteractionResult>map((source) -> {
                            ItemAccess itemAccess = ItemAccess.forStack(itemStack);
                            FluidStack moved = IModHelpersNeoForge.get().getFluidHelpers().move(source, itemStack.getCapability(Capabilities.Fluid.ITEM, itemAccess), Integer.MAX_VALUE, context.getPlayer(), false, false);
                            if (!moved.isEmpty() && context.getLevel().isClientSide()) {
                                ParticleHelpers.spawnBloodSplashParticles(context.getLevel(), context.getClickedPos(), 5, 1 + random.nextInt(2));
                            }
                            return InteractionResult.SUCCESS.heldItemTransformedTo(itemAccess.getResource().toStack(itemAccess.getAmount()));
                        }).orElse(InteractionResult.PASS);
            }
        }
        return InteractionResult.PASS;
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
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!player.isCrouching()) {
            return super.use(world, player, hand);
        } else {
            HitResult target = this.getPlayerPOVHitResult(world, player, ClipContext.Fluid.ANY);
            if(target == null || target.getType() == HitResult.Type.MISS) {
                if(!world.isClientSide()) {
                    ItemHelpers.toggleActivation(itemStack);
                }
            }
        }
        return InteractionResult.SUCCESS.heldItemTransformedTo(itemStack);
    }

    /**
     * Fill all the Blood Extractors on a player's hotbar for a given fluid amount.
     * It will fill Blood Extractors until the predefined blood amount is depleted.
     * (It fills on at a time).
     * @param player The player to the the Blood Extractors for.
     * @param minimumMB The minimum amount to fill. (inclusive)
     * @param maximumMB The maximum amount to fill. (exclusive)
     */
    public static void fillForAllBloodExtractors(Player player, int minimumMB, int maximumMB) {
        int toFill = minimumMB + player.getRandom().nextInt(Math.max(1, maximumMB - minimumMB));
        PlayerInventoryIterator it = new PlayerInventoryIterator(player);
        while(it.hasNext() && toFill > 0) {
            Pair<Integer, ItemStack> itemStackLocation = it.nextIndexed();
            ItemStack itemStack = itemStackLocation.getRight();
            if(!itemStack.isEmpty() && itemStack.getItem() instanceof ItemBloodExtractor) {
                ResourceHandler<FluidResource> fluidHandler = new ItemAccessItemLocation(player, new ItemLocation(InventoryLocationPlayer.getInstance(), itemStackLocation.getLeft())).getCapability(Capabilities.Fluid.ITEM);
                try (var tx = Transaction.openRoot()) {
                    toFill -= fluidHandler.insert(FluidResource.of(RegistryEntries.FLUID_BLOOD), toFill, tx);
                    tx.commit();
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        if(ItemHelpers.isActivated(itemStack)) {
            ItemHelpers.updateAutoFill(itemStack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(itemStack)), itemStack, level, entity, ItemBloodExtractorConfig.autoFillBuckets);
        }
        super.inventoryTick(itemStack, level, entity, slot);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    public void bloodObtainEvent(LivingDeathEvent event) {
        Entity e = event.getSource().getEntity();
        if(e != null && e instanceof ServerPlayer && !e.level().isClientSide()
                && event.getEntity() != null) {
            float boost = 1.0F;
            ServerPlayer player = (ServerPlayer) e;
            InteractionHand hand = player.getUsedItemHand();
            if(hand != null && player.getItemInHand(hand) != null
                    && player.getItemInHand(hand).getItem() instanceof ItemVeinSword) {
                boost = (float) ItemVeinSwordConfig.extractionBoost;
            }
            float health = event.getEntity().getMaxHealth();
            int minimumMB = Mth.floor(health * (float) ItemBloodExtractorConfig.minimumMobMultiplier * boost);
            int maximumMB = Mth.floor(health * (float) ItemBloodExtractorConfig.maximumMobMultiplier * boost);
            ItemBloodExtractor.fillForAllBloodExtractors(player, minimumMB, maximumMB);
        }
    }

}
