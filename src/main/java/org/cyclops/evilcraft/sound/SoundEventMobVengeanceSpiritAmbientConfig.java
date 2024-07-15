package org.cyclops.evilcraft.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class SoundEventMobVengeanceSpiritAmbientConfig extends org.cyclops.cyclopscore.config.extendedconfig.SoundEventConfig {
    public SoundEventMobVengeanceSpiritAmbientConfig() {
        super(
                EvilCraft._instance,
                "mob_vengeancespirit_ambient",
                (eConfig) -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(eConfig.getMod().getModId(), eConfig.getNamedId()))
        );
    }
}
