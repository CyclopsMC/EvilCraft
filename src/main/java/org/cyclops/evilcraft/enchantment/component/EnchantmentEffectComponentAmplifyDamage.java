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

import java.util.Random;

/**
 * @author rubensworks
 */
public class EnchantmentEffectComponentAmplifyDamage {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void livingDamage(LivingDamageEvent.Post event) {
        if (!event.getEntity().getCommandSenderWorld().isClientSide() && event.getSource().getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getSource().getEntity();
            InteractionHand hand = entity.getUsedItemHand();
            if (hand != null) {
                ItemStack itemStack = entity.getItemInHand(hand);
                if (amplifyDamage(itemStack)) {
                    entity.setItemInHand(hand, itemStack);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void breakingEvent(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().level().isClientSide()) {
            InteractionHand hand = event.getPlayer().getUsedItemHand();
            if (hand != null) {
                ItemStack itemStack = event.getPlayer().getItemInHand(hand);
                if (amplifyDamage(itemStack)) {
                    event.getPlayer().setItemInHand(hand, itemStack);
                }
            }
        }
    }

    public boolean amplifyDamage(ItemStack itemStack) {
        Wrapper<Boolean> applied = new Wrapper<>(false);
        EnchantmentHelpers.runIterationOnItem(itemStack, (enchantment, level) -> {
            int modifier = enchantment.value().effects().getOrDefault(RegistryEntries.ENCHANTMENTEFFECT_COMPONENT_AMPLIFY_DAMAGE.get(), 0);
            if (modifier > 0) {
                int newDamage = itemStack.getDamageValue() + modifier;
                Random random = new Random();
                if(random.nextFloat() < 0.6F ? false : random.nextInt(level + 1) > 0
                        && newDamage <= itemStack.getMaxDamage()) {
                    itemStack.setDamageValue(newDamage);
                    applied.set(true);
                }
            }
        });
        return applied.get();
    }

}
