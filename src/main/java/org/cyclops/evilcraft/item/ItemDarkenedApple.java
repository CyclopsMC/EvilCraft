package org.cyclops.evilcraft.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import org.cyclops.evilcraft.RegistryEntries;

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
                .food((new Food.Builder())
                        .hunger(0)
                        .saturation(0)
                        .setAlwaysEdible()
                        .effect(() -> new EffectInstance(RegistryEntries.POTION_PALING, POTION_DURATION * 20, POTION_AMPLIFIER), 1)
                        .build()));
    }

    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 64;
    }

    public ActionResultType itemInteractionForEntity(ItemStack itemStack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if(entity instanceof AnimalEntity && entity.getMaxHealth() <= 10) {
            entity.addPotionEffect(new EffectInstance(RegistryEntries.POTION_PALING, POTION_DURATION * 20, POTION_AMPLIFIER));
            itemStack.shrink(1);
            return ActionResultType.CONSUME;
        }
        return super.itemInteractionForEntity(itemStack, player, entity, hand);
    }

}
