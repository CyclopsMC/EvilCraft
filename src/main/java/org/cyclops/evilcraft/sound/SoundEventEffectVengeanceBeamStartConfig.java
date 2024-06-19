package org.cyclops.evilcraft.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class SoundEventEffectVengeanceBeamStartConfig extends org.cyclops.cyclopscore.config.extendedconfig.SoundEventConfig {
    public SoundEventEffectVengeanceBeamStartConfig() {
        super(
                EvilCraft._instance,
                "effect_vengeancebeam_start",
                (eConfig) -> SoundEvent.createVariableRangeEvent(new ResourceLocation(eConfig.getMod().getModId(), eConfig.getNamedId()))
        );
    }
}
