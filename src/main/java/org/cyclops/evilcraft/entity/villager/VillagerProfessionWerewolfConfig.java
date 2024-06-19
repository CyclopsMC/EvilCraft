package org.cyclops.evilcraft.entity.villager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the werewolf villager profession.
 * @author rubensworks
 */
public class VillagerProfessionWerewolfConfig extends VillagerConfig {

    public VillagerProfessionWerewolfConfig() {
        super(
                EvilCraft._instance,
                "werewolf",
                eConfig -> new VillagerProfession(
                        new ResourceLocation(Reference.MOD_ID, eConfig.getNamedId()).toString(),
                        (poiType) -> poiType.is(PoiTypes.BUTCHER),
                        (poiType) -> poiType.is(PoiTypes.BUTCHER),
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        SoundEvents.VILLAGER_WORK_BUTCHER
                )
        );
        NeoForge.EVENT_BUS.addListener(this::onTrades);
    }

    public void onTrades(VillagerTradesEvent event) {
        if (event.getType() == getInstance()) {
            // Villager accepts these for emeralds
            event.getTrades().get(2).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_DARK_GEM.get(), 10, 50, 2));
            event.getTrades().get(2).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_HARDENED_BLOOD_SHARD.get(), 20, 50, 2));
            event.getTrades().get(2).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_POISON_SAC.get(), 3, 50, 2));
            event.getTrades().get(3).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_BLOOK.get(), 2, 30, 5));
            event.getTrades().get(3).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_INVERTED_POTENTIA.get(), 2, 30, 6));
            event.getTrades().get(4).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_INVERTED_POTENTIA_EMPOWERED.get(), 1, 25, 10));
            event.getTrades().get(5).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_BLOOD_INFUSION_CORE.get(), 3, 20, 10));
            event.getTrades().get(5).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_WEREWOLF_BONE.get(), 2, 20, 20));
            event.getTrades().get(5).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_WEREWOLF_FUR.get(), 1, 20, 20));

            // Villager offers these for emeralds
            // Args: output item, emeralds, items, xp
            event.getTrades().get(2).add(new ItemsForEmeraldsTrade(RegistryEntries.ITEM_DARK_GEM_CRUSHED.get(), 1, 5, 10));
            event.getTrades().get(3).add(new ItemsForEmeraldsTrade(RegistryEntries.ITEM_UNDEAD_SAPLING.get(), 1, 3, 10));
            event.getTrades().get(4).add(new ItemsForEmeraldsTrade(RegistryEntries.ITEM_VENGEANCE_FOCUS.get(), 3, 2, 10));
            event.getTrades().get(4).add(new ItemsForEmeraldsTrade(RegistryEntries.ITEM_BOX_OF_ETERNAL_CLOSURE.get(), 7, 1, 10));
            event.getTrades().get(5).add(new ItemsForEmeraldsTrade(RegistryEntries.ITEM_GARMONBOZIA.get(), 10, 1, 30));
        }
    }

}
