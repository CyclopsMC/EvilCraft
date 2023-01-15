package org.cyclops.evilcraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

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
        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(resourceLocation);
        registry.register(resourceLocation, soundEvent);
        return soundEvent;
    }

    @SubscribeEvent
    public static void registerSoundEvents(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.SOUND_EVENTS)) {
            effect_vengeancebeam_base = getRegisteredSoundEvent(event.getForgeRegistry(), "effect.vengeancebeam.base");
            effect_vengeancebeam_start = getRegisteredSoundEvent(event.getForgeRegistry(), "effect.vengeancebeam.start");
            effect_vengeancebeam_stop = getRegisteredSoundEvent(event.getForgeRegistry(), "effect.vengeancebeam.stop");
            mob_vengeancespirit_ambient = getRegisteredSoundEvent(event.getForgeRegistry(), "mob.vengeancespirit.ambient");
            mob_vengeancespirit_death = getRegisteredSoundEvent(event.getForgeRegistry(), "mob.vengeancespirit.death");
            effect_box_beam = getRegisteredSoundEvent(event.getForgeRegistry(), "effect.box.beam");
            effect_page_flipsingle = getRegisteredSoundEvent(event.getForgeRegistry(), "effect.page.flipsingle");
            effect_page_flipmultiple = getRegisteredSoundEvent(event.getForgeRegistry(), "effect.page.flipmultiple");
        }
    }

}
