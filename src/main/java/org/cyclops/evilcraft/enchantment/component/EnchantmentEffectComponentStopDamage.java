package org.cyclops.evilcraft.enchantment.component;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.cyclops.cyclopscore.helper.EnchantmentHelpers;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.core.algorithm.Wrapper;

/**
 * @author rubensworks
 */
public class EnchantmentEffectComponentStopDamage {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void unusingEvent(LivingDamageEvent.Pre event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() && event.getContainer().getSource().getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getContainer().getSource().getEntity();
            InteractionHand hand = entity.getUsedItemHand();
            if (hand != null) {
                ItemStack itemStack = entity.getItemInHand(hand);
                if (stopDamage(itemStack)) {
                    event.getContainer().setNewDamage(0);
                    entity.releaseUsingItem();
                    entity.setItemInHand(hand, itemStack);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void unusingEvent(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().level().isClientSide()) {
            InteractionHand hand = event.getPlayer().getUsedItemHand();
            if (hand != null) {
                ItemStack itemStack = event.getPlayer().getItemInHand(hand);
                if (stopDamage(itemStack)) {
                    event.getPlayer().setItemInHand(hand, itemStack);
                    event.setCanceled(true);
                    event.getPlayer().releaseUsingItem();
                }
            }
        }
    }

    public boolean stopDamage(ItemStack itemStack) {
        Wrapper<Boolean> ret = new Wrapper<Boolean>(false);
        EnchantmentHelpers.runIterationOnItem(itemStack, (enchantment, level) -> {
            if (enchantment.value().effects().getOrDefault(RegistryEntries.ENCHANTMENTEFFECT_COMPONENT_STOP_DAMAGE.get(), false)) {
                int damageBorder = itemStack.getMaxDamage() - 5;
                if(itemStack.getDamageValue() >= damageBorder) {
                    itemStack.setDamageValue(damageBorder);
                    ret.set(true);
                }
            }
        });
        return ret.get();
    }

}
