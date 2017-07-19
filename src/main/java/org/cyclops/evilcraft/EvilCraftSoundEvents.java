package org.cyclops.evilcraft;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * All mod SoundEvent references.
 * @author rubensworks
 */
public class EvilCraftSoundEvents {

    public static SoundEvent effect_vengeancebeam_base;
    public static SoundEvent effect_vengeancebeam_start;
    public static SoundEvent effect_vengeancebeam_stop;
    public static SoundEvent mob_vengeancespirit_ambient;
    public static SoundEvent mob_vengeancespirit_death;
    public static SoundEvent effect_box_beam;
    public static SoundEvent effect_page_flipsingle;
    public static SoundEvent effect_page_flipmultiple;

    private static SoundEvent getRegisteredSoundEvent(IForgeRegistry<SoundEvent> registry,  String id) {
        ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, id);
        SoundEvent soundEvent = new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
        registry.register(soundEvent);
        return soundEvent;
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegistryEvent.Register event) {
        if (event.getRegistry() == ForgeRegistries.SOUND_EVENTS) {
            effect_vengeancebeam_base = getRegisteredSoundEvent(event.getRegistry(), "effect.vengeancebeam.base");
            effect_vengeancebeam_start = getRegisteredSoundEvent(event.getRegistry(), "effect.vengeancebeam.start");
            effect_vengeancebeam_stop = getRegisteredSoundEvent(event.getRegistry(), "effect.vengeancebeam.stop");
            mob_vengeancespirit_ambient = getRegisteredSoundEvent(event.getRegistry(), "mob.vengeancespirit.ambient");
            mob_vengeancespirit_death = getRegisteredSoundEvent(event.getRegistry(), "mob.vengeancespirit.death");
            effect_box_beam = getRegisteredSoundEvent(event.getRegistry(), "effect.box.beam");
            effect_page_flipsingle = getRegisteredSoundEvent(event.getRegistry(), "effect.page.flipsingle");
            effect_page_flipmultiple = getRegisteredSoundEvent(event.getRegistry(), "effect.page.flipmultiple");
        }
    }

}
