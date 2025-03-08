package org.cyclops.evilcraft.potion;

import org.cyclops.cyclopscore.config.extendedconfig.EffectConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for {@link PotionPaling}.
 * @author rubensworks
 *
 */
public class PotionPalingConfig extends EffectConfigCommon<IModBase> {

    public PotionPalingConfig() {
        super(
            EvilCraft._instance,
            "paling",
            eConfig -> new PotionPaling()
        );
    }

}
