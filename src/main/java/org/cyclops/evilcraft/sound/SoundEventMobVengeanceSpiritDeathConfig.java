package org.cyclops.evilcraft.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class SoundEventMobVengeanceSpiritDeathConfig extends org.cyclops.cyclopscore.config.extendedconfig.SoundEventConfig {
    public SoundEventMobVengeanceSpiritDeathConfig() {
        super(
                EvilCraft._instance,
                "mob_vengeancespirit_death",
                (eConfig) -> SoundEvent.createVariableRangeEvent(new ResourceLocation(eConfig.getMod().getModId(), eConfig.getNamedId()))
        );
    }
}
