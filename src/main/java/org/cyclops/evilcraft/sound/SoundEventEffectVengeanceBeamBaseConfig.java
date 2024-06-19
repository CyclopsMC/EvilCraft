package org.cyclops.evilcraft.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class SoundEventEffectVengeanceBeamBaseConfig extends org.cyclops.cyclopscore.config.extendedconfig.SoundEventConfig {
    public SoundEventEffectVengeanceBeamBaseConfig() {
        super(
                EvilCraft._instance,
                "effect_vengeancebeam_base",
                (eConfig) -> SoundEvent.createVariableRangeEvent(new ResourceLocation(eConfig.getMod().getModId(), eConfig.getNamedId()))
        );
    }
}
