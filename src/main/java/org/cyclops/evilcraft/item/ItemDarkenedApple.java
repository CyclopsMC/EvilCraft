package org.cyclops.evilcraft.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import org.cyclops.evilcraft.RegistryEntries;

import net.minecraft.world.item.Item.Properties;

/**
 * A dark apple that will apply a killing potion effect to the entity eating the apple.
 * After the potion effect is over, a portal will be spawned.
 * @author rubensworks
 *
 */
public class ItemDarkenedApple extends Item {

    private static final int POTION_DURATION = 30;
    private static final int POTION_AMPLIFIER = 4;

    public ItemDarkenedApple(Properties properties) {
        super(properties
                .food((new FoodProperties.Builder())
                        .nutrition(0)
                        .saturationMod(0)
                        .alwaysEat()
                        .effect(() -> new MobEffectInstance(RegistryEntries.POTION_PALING, POTION_DURATION * 20, POTION_AMPLIFIER), 1)
                        .build()));
    }

    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 64;
    }

    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity entity, InteractionHand hand) {
        if(entity instanceof Animal && entity.getMaxHealth() <= 10) {
            entity.addEffect(new MobEffectInstance(RegistryEntries.POTION_PALING, POTION_DURATION * 20, POTION_AMPLIFIER));
            itemStack.shrink(1);
            return InteractionResult.CONSUME;
        }
        return super.interactLivingEntity(itemStack, player, entity, hand);
    }

}
