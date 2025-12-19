package org.cyclops.evilcraft.sound;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import org.cyclops.cyclopscore.config.extendedconfig.SoundEventConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;

/**
 * @author rubensworks
 */
public class SoundEventEffectBoxBeamConfig extends SoundEventConfigCommon<IModBase> {
    public SoundEventEffectBoxBeamConfig() {
        super(
                EvilCraft._instance,
                "effect_box_beam",
                (eConfig) -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(eConfig.getMod().getModId(), eConfig.getNamedId()))
        );
    }
}
