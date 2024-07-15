package org.cyclops.evilcraft.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class SoundEventEffectPageFlipMultipleConfig extends org.cyclops.cyclopscore.config.extendedconfig.SoundEventConfig {
    public SoundEventEffectPageFlipMultipleConfig() {
        super(
                EvilCraft._instance,
                "effect_page_flipmultiple",
                (eConfig) -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(eConfig.getMod().getModId(), eConfig.getNamedId()))
        );
    }
}
