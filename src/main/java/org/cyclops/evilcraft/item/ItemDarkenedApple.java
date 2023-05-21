package org.cyclops.evilcraft.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
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
                .food((new FoodProperties.Builder())
                        .nutrition(0)
                        .saturationMod(0)
                        .alwaysEat()
                        .effect(() -> new MobEffectInstance(RegistryEntries.POTION_PALING, POTION_DURATION * 20, POTION_AMPLIFIER), 1)
                        .build()));
        MinecraftForge.EVENT_BUS.addListener(this::onInteract);
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 64;
    }

    public void onInteract(PlayerInteractEvent.EntityInteract event) {
        Entity entity = event.getTarget();
        if (event.getItemStack().getItem() == this && entity instanceof Animal animal) {
            animal.addEffect(new MobEffectInstance(RegistryEntries.POTION_PALING, POTION_DURATION * 20, POTION_AMPLIFIER));
            event.getItemStack().shrink(1);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.CONSUME);
        }
    }

}
