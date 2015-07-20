package org.cyclops.evilcraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.core.config.configurable.ConfigurableItemFood;
import org.cyclops.evilcraft.potion.PotionPalingConfig;

/**
 * A dark apple that will apply a killing potion effect to the entity eating the apple.
 * After the potion effect is over, a portal will be spawned.
 * @author rubensworks
 *
 */
public class DarkenedApple extends ConfigurableItemFood {

    private static final int POTION_ID = PotionPalingConfig._instance.ID;
    private static final int POTION_DURATION = 30;
    private static final int POTION_AMPLIFIER = 4;

    private static DarkenedApple _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static DarkenedApple getInstance() {
        return _instance;
    }

    public DarkenedApple(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig, 0, 0, false);
        this.setAlwaysEdible();
        this.setPotionEffect(POTION_ID, POTION_DURATION, POTION_AMPLIFIER, 1);
    }

    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 64;
    }

    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase entity) {
        if(entity instanceof IAnimals && entity.getMaxHealth() <= 10) {
            entity.addPotionEffect(new PotionEffect(POTION_ID, POTION_DURATION * 20, POTION_AMPLIFIER));
            --itemStack.stackSize;
            return true;
        }
        return super.itemInteractionForEntity(itemStack, player, entity);
    }

}
