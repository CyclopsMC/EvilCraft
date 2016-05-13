package org.cyclops.evilcraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.entity.effect.EntityAttackVengeanceBeam;

/**
 * Focus that is able attack vengeance spirits.
 * @author rubensworks
 *
 */
public class PiercingVengeanceFocus extends AbstractFocus {

    private static PiercingVengeanceFocus _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static PiercingVengeanceFocus getInstance() {
        return _instance;
    }

    public PiercingVengeanceFocus(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    protected EntityThrowable newBeamEntity(EntityLivingBase player) {
        return new EntityAttackVengeanceBeam(player.worldObj, player);
    }
}
