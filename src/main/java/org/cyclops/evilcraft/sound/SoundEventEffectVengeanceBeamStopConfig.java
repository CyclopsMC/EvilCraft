package org.cyclops.evilcraft.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class SoundEventEffectVengeanceBeamStopConfig extends org.cyclops.cyclopscore.config.extendedconfig.SoundEventConfig {
    public SoundEventEffectVengeanceBeamStopConfig() {
        super(
                EvilCraft._instance,
                "effect_vengeancebeam_stop",
                (eConfig) -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(eConfig.getMod().getModId(), eConfig.getNamedId()))
        );
    }
}
