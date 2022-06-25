package org.cyclops.evilcraft.entity.villager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.VillagerTradesEvent;
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
        MinecraftForge.EVENT_BUS.addListener(this::onTrades);
    }

    public void onTrades(VillagerTradesEvent event) {
        if (event.getType() == getInstance()) {
            // Villager accepts these for emeralds
            event.getTrades().get(2).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_DARK_GEM, 10, 50, 2));
            event.getTrades().get(2).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_HARDENED_BLOOD_SHARD, 20, 50, 2));
            event.getTrades().get(2).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_POISON_SAC, 3, 50, 2));
            event.getTrades().get(3).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_BLOOK, 2, 30, 5));
            event.getTrades().get(3).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_INVERTED_POTENTIA, 2, 30, 6));
            event.getTrades().get(4).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_INVERTED_POTENTIA_EMPOWERED, 1, 25, 10));
            event.getTrades().get(5).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_BLOOD_INFUSION_CORE, 3, 20, 10));
            event.getTrades().get(5).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_WEREWOLF_BONE, 2, 20, 20));
            event.getTrades().get(5).add(new EmeraldForItemsTrade(RegistryEntries.ITEM_WEREWOLF_FUR, 1, 20, 20));

            // Villager offers these for emeralds
            // Args: output item, emeralds, items, xp
            event.getTrades().get(2).add(new ItemsForEmeraldsTrade(RegistryEntries.ITEM_DARK_GEM_CRUSHED, 1, 5, 10));
            event.getTrades().get(3).add(new ItemsForEmeraldsTrade(RegistryEntries.ITEM_UNDEAD_SAPLING, 1, 3, 10));
            event.getTrades().get(4).add(new ItemsForEmeraldsTrade(RegistryEntries.ITEM_VENGEANCE_FOCUS, 3, 2, 10));
            event.getTrades().get(4).add(new ItemsForEmeraldsTrade(RegistryEntries.ITEM_BOX_OF_ETERNAL_CLOSURE, 7, 1, 10));
            event.getTrades().get(5).add(new ItemsForEmeraldsTrade(RegistryEntries.ITEM_GARMONBOZIA, 10, 1, 30));
        }
    }

}
