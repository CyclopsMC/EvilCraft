package org.cyclops.evilcraft.item;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.evilcraft.core.item.ItemBloodContainer;

import javax.annotation.Nullable;
import java.util.Iterator;

/**
 * Ring that can enable sight into the vengeance spirit realm.
 * @author rubensworks
 *
 */
public class ItemInvigoratingPendant extends ItemBloodContainer {

    private static final int TICK_MODULUS = IModHelpers.get().getMinecraftHelpers().getSecondInTicks() / 2;

    public ItemInvigoratingPendant(Item.Properties properties) {
        super(properties, ItemInvigoratingPendantConfig.capacity);
    }

    /**
     * Clear the bad effects of given player.
     * Each 'tick', a certain amount of bad effect duration reduction is reserved.
     * Each found effect it's duration is reduced by as much as possible (not larger than the reserved amount)
     * and the inner tank is drained according to how much was reduced.
     * If the reserved duration is not zero at the end, the next bad effect will be taken.
     * @param itemStack The pendant to drain.
     * @param player The player to receive the powers.
     */
    public void clearBadEffects(ItemStack itemStack, Player player) {
        int amount = ItemInvigoratingPendantConfig.usage;
        if(canConsume(amount, itemStack, player)) {

            int originalReducableDuration = ItemInvigoratingPendantConfig.reduceDuration * IModHelpers.get().getMinecraftHelpers().getSecondInTicks();
            int reducableDuration = originalReducableDuration;

            Iterator<MobEffectInstance> it = Lists.newLinkedList(player.getActiveEffects()).iterator();
            while(reducableDuration > 0 && it.hasNext() && canConsume(amount, itemStack, player)) {
                MobEffectInstance effect = it.next();
                Holder<MobEffect> potion = effect.getEffect();

                boolean shouldClear = true;
                if(potion != null) {
                    shouldClear = potion.value().getCategory() == MobEffectCategory.HARMFUL;
                }
                shouldClear = shouldClear & !effect.isAmbient();

                if(shouldClear) {
                    int reductionMultiplier = Math.max(1, effect.getAmplifier() + 1);
                    int reducableDurationForThisEffect = reducableDuration / reductionMultiplier;
                    int remaining = effect.getDuration();
                    int toReduce = Math.min(reducableDurationForThisEffect, remaining);
                    int toDrain = amount;

                    reducableDuration -= toReduce;
                    if(remaining == toReduce) {
                        player.removeEffect(potion);
                    } else {
                        effect.duration = remaining - toReduce;
                        player.onEffectUpdated(effect, true, null);
                        toDrain = (int) Math.ceil((double) (reductionMultiplier * amount)
                                * ((double) toReduce / (double) originalReducableDuration));
                    }
                    consume(toDrain, itemStack, player);
                }
            }
        }

        if(ItemInvigoratingPendantConfig.fireUsage >= 0 && player.isOnFire() &&
                canConsume(ItemInvigoratingPendantConfig.fireUsage, itemStack, player)) {
            player.clearFire();
            consume(ItemInvigoratingPendantConfig.fireUsage, itemStack, player);
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        if(entity instanceof Player player
                && IModHelpers.get().getWorldHelpers().efficientTick(level, TICK_MODULUS, entity.getId())) {
            clearBadEffects(itemStack, player);
        }
        super.inventoryTick(itemStack, level, entity, slot);
    }

}
