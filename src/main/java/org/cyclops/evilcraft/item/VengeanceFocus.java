package org.cyclops.evilcraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ItemConfig;
import org.cyclops.evilcraft.entity.effect.EntityAntiVengeanceBeam;

/**
 * Focus that is able to direct rays of the sun to entangle vengeance spirits.
 * @author rubensworks
 *
 */
public class VengeanceFocus extends AbstractFocus {
    
    private static VengeanceFocus _instance = null;
    
    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static VengeanceFocus getInstance() {
        return _instance;
    }

    public VengeanceFocus(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
    }

    @Override
    protected EntityThrowable newBeamEntity(EntityLivingBase player) {
        return new EntityAntiVengeanceBeam(player.world, player);
    }
}
