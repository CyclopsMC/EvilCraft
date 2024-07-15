package org.cyclops.evilcraft.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class SoundEventEffectBoxBeamConfig extends org.cyclops.cyclopscore.config.extendedconfig.SoundEventConfig {
    public SoundEventEffectBoxBeamConfig() {
        super(
                EvilCraft._instance,
                "effect_box_beam",
                (eConfig) -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(eConfig.getMod().getModId(), eConfig.getNamedId()))
        );
    }
}
