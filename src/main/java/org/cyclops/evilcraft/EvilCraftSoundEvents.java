package org.cyclops.evilcraft;

import net.minecraft.init.Bootstrap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * All mod SoundEvent references.
 * @author rubensworks
 */
public class EvilCraftSoundEvents {

    public static final SoundEvent effect_vengeancebeam_base;
    public static final SoundEvent effect_vengeancebeam_start;
    public static final SoundEvent effect_vengeancebeam_stop;
    public static final SoundEvent mob_vengeancespirit_ambient;
    public static final SoundEvent mob_vengeancespirit_death;
    public static final SoundEvent effect_box_beam;
    public static final SoundEvent effect_page_flipsingle;
    public static final SoundEvent effect_page_flipmultiple;

    private static SoundEvent getRegisteredSoundEvent(String id) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, id);
        return GameRegistry.register(new SoundEvent(resourceLocation).setRegistryName(resourceLocation));
    }

    static {
        if (!Bootstrap.isRegistered()) {
            throw new RuntimeException("Accessed Sounds before Bootstrap!");
        } else {
            effect_vengeancebeam_base = getRegisteredSoundEvent("effect.vengeancebeam.base");
            effect_vengeancebeam_start = getRegisteredSoundEvent("effect.vengeancebeam.start");
            effect_vengeancebeam_stop = getRegisteredSoundEvent("effect.vengeancebeam.stop");
            mob_vengeancespirit_ambient = getRegisteredSoundEvent("mob.vengeancespirit.ambient");
            mob_vengeancespirit_death = getRegisteredSoundEvent("mob.vengeancespirit.death");
            effect_box_beam = getRegisteredSoundEvent("effect.box.beam");
            effect_page_flipsingle = getRegisteredSoundEvent("effect.page.flipsingle");
            effect_page_flipmultiple = getRegisteredSoundEvent("effect.page.flipmultiple");
        }
    }

}
