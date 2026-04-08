package org.cyclops.evilcraft.entity.villager;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.extendedconfig.VillagerConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the werewolf villager profession.
 * @author rubensworks
 */
public class VillagerProfessionWerewolfConfig extends VillagerConfigCommon<IModBase> {

    @ConfigurablePropertyCommon(category = "mob", comment = "If villagers struck by lightning should have a 50% chance of becoming a werewolf villager", isCommandable = true)
    public static boolean convertOnLightning = true;

    public VillagerProfessionWerewolfConfig() {
        super(
                EvilCraft._instance,
                "werewolf",
                eConfig -> {
                    Int2ObjectMap<ResourceKey<TradeSet>> tradeSets = new Int2ObjectOpenHashMap<>();
                    for (int level = 1; level <= 4; level++) {
                        tradeSets.put(level, ResourceKey.create(Registries.TRADE_SET,
                                Identifier.fromNamespaceAndPath(Reference.MOD_ID, "werewolf/level_" + level)));
                    }
                    return new VillagerProfession(
                            Component.translatable("entity." + Reference.MOD_ID + ".villager." + eConfig.getNamedId()),
                            (poiType) -> poiType.is(RegistryEntries.POI_WEREWOLVIAN),
                            (poiType) -> poiType.is(RegistryEntries.POI_WEREWOLVIAN),
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_BUTCHER,
                            tradeSets
                    );
                }
        );
    }

}
