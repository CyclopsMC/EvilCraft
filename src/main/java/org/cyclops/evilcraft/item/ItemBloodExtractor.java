package org.cyclops.evilcraft.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.inventory.PlayerInventoryIterator;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodStain;
import org.cyclops.evilcraft.client.particle.ParticleBloodSplash;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;

import java.util.List;

/**
 * Can extract blood from attacking mobs and {@link BlockBloodStain}.
 * @author rubensworks
 *
 */
public class ItemBloodExtractor extends ItemBloodContainer {

    public ItemBloodExtractor(Item.Properties properties) {
        super(properties, ItemBloodExtractorConfig.containerSize);
        setPlaceFluids(true);
        MinecraftForge.EVENT_BUS.addListener(this::bloodObtainEvent);

    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
        Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();
        if(context.getPlayer().isCrouching()) {
            if(block instanceof BlockBloodStain) {
                RandomSource random = context.getLevel().random;

                // Fill the extractor a bit
                BlockEntityHelpers.getCapability(context.getLevel(), context.getClickedPos(), ForgeCapabilities.FLUID_HANDLER)
                        .ifPresent((source) -> {
                            FluidStack moved = FluidUtil.tryFluidTransfer(FluidUtil.getFluidHandler(itemStack).orElse(null), source, Integer.MAX_VALUE, true);
                            if (!moved.isEmpty() && context.getLevel().isClientSide()) {
                                ParticleBloodSplash.spawnParticles(context.getLevel(), context.getClickedPos(), 5, 1 + random.nextInt(2));
                            }
                        });
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isFoil(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
                getDescriptionId() + ".info.auto_supply");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
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
        return MinecraftHelpers.successAction(itemStack);
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
            ItemStack itemStack = it.next();
            if(!itemStack.isEmpty() && itemStack.getItem() instanceof ItemBloodExtractor) {
                IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(itemStack).orElse(null);
                toFill -= fluidHandler.fill(new FluidStack(RegistryEntries.FLUID_BLOOD, toFill), IFluidHandler.FluidAction.EXECUTE);
                it.replace(fluidHandler.getContainer());
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int itemSlot, boolean par5) {
        if(ItemHelpers.isActivated(itemStack)) {
            ItemHelpers.updateAutoFill(FluidUtil.getFluidHandler(itemStack).orElse(null), world, entity, ItemBloodExtractorConfig.autoFillBuckets);
        }
        super.inventoryTick(itemStack, world, entity, itemSlot, par5);
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
