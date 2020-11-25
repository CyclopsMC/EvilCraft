package org.cyclops.evilcraft.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.cyclopscore.inventory.PlayerInventoryIterator;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.block.BlockBloodStain;
import org.cyclops.evilcraft.client.particle.ParticleBloodSplash;
import org.cyclops.evilcraft.core.helper.ItemHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;

import java.util.List;
import java.util.Random;

/**
 * Can extract blood from attacking mobs and {@link BlockBloodStain}.
 * @author rubensworks
 *
 */
public class ItemBloodExtractor extends ItemBloodContainer {

    public ItemBloodExtractor(Item.Properties properties) {
        super(properties, ItemBloodExtractorConfig.containerSize);
        setPlaceFluids(true);
        MinecraftForge.EVENT_BUS.register(this);

    }
    
    @Override
    public ActionResultType onItemUseFirst(ItemStack itemStack, ItemUseContext context) {
        Block block = context.getWorld().getBlockState(context.getPos()).getBlock();
        if(context.getPlayer().isCrouching()) {
	        if(block instanceof BlockBloodStain) {
	            Random random = context.getWorld().rand;

                // Fill the extractor a bit
                TileHelpers.getCapability(context.getWorld(), context.getPos(), CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                        .ifPresent((source) -> {
                            FluidStack moved = FluidUtil.tryFluidTransfer(FluidUtil.getFluidHandler(itemStack).orElse(null), source, Integer.MAX_VALUE, true);
                            if (!moved.isEmpty() && context.getWorld().isRemote()) {
                                ParticleBloodSplash.spawnParticles(context.getWorld(), context.getPos(), 5, 1 + random.nextInt(2));
                            }
                        });
	            return ActionResultType.PASS;
	        }
        }
        return ActionResultType.PASS;
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
        return ItemHelpers.isActivated(itemStack);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        L10NHelpers.addStatusInfo(list, ItemHelpers.isActivated(itemStack),
                getTranslationKey() + ".info.auto_supply");
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if(!player.isCrouching()) {
            return super.onItemRightClick(world, player, hand);
        } else {
            RayTraceResult target = this.rayTrace(world, player, RayTraceContext.FluidMode.ANY);
            if(target == null || target.getType() == RayTraceResult.Type.MISS) {
        		if(!world.isRemote()) {
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
    public static void fillForAllBloodExtractors(PlayerEntity player, int minimumMB, int maximumMB) {
        int toFill = minimumMB + random.nextInt(Math.max(1, maximumMB - minimumMB));
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
    public void inventoryTick(ItemStack itemStack, World world, Entity entity, int itemSlot, boolean par5) {
    	if(ItemHelpers.isActivated(itemStack)) {
    		ItemHelpers.updateAutoFill(FluidUtil.getFluidHandler(itemStack).orElse(null), world, entity, ItemBloodExtractorConfig.autoFillBuckets);
    	}
        super.inventoryTick(itemStack, world, entity, itemSlot, par5);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void bloodObtainEvent(LivingDeathEvent event) {
        Entity e = event.getSource().getTrueSource();
        if(e != null && e instanceof ServerPlayerEntity && !e.world.isRemote()
                && event.getEntityLiving() != null) {
            float boost = 1.0F;
            ServerPlayerEntity player = (ServerPlayerEntity) e;
            Hand hand = player.getActiveHand();
            if(player.getHeldItem(hand) != null
                    && player.getHeldItem(hand).getItem() instanceof ItemVeinSword) {
                boost = (float) ItemVeinSwordConfig.extractionBoost;
            }
            float health = event.getEntityLiving().getMaxHealth();
            int minimumMB = MathHelper.floor(health * (float) ItemBloodExtractorConfig.minimumMobMultiplier * boost);
            int maximumMB = MathHelper.floor(health * (float) ItemBloodExtractorConfig.maximumMobMultiplier * boost);
            ItemBloodExtractor.fillForAllBloodExtractors(player, minimumMB, maximumMB);
        }
    }

}
