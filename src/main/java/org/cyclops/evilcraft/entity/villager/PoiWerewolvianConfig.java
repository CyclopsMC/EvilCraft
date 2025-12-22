package org.cyclops.evilcraft.entity.villager;

import com.google.common.collect.Sets;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.config.extendedconfig.PoiConfigCommon;
import org.cyclops.cyclopscore.helper.IStructureHelpersNeoForge;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

import java.util.HashSet;

/**
 * Config for the werewolf villager profession.
 * @author rubensworks
 */
public class PoiWerewolvianConfig extends PoiConfigCommon<EvilCraft> {

    public PoiWerewolvianConfig() {
        super(
                EvilCraft._instance,
                "werewolvian",
                eConfig -> {
                    HashSet<BlockState> blockStates = Sets.newHashSet();
                    blockStates.addAll(RegistryEntries.BLOCK_BLOOD_INFUSER.value().getStateDefinition().getPossibleStates());
                    blockStates.addAll(RegistryEntries.BLOCK_BLOOD_CHEST.value().getStateDefinition().getPossibleStates());
                    blockStates.addAll(RegistryEntries.BLOCK_PURIFIER.value().getStateDefinition().getPossibleStates());
                    return new PoiType(blockStates, 1, 1);
                }
        );
    }

    @Override
    public void onRegistered() {
        super.onRegistered();
        IStructureHelpersNeoForge sh = getMod().getModHelpers().getStructureHelpers();
        for (String biome : new String[]{"plains", "snowy", "savanna", "desert", "taiga"}) {
            sh.addToStructureTemplatePool(
                    Identifier.withDefaultNamespace("village/" + biome + "/houses"),
                    Identifier.fromNamespaceAndPath(getMod().getModId(), "village/werewolvian_mansion")
            );
        }
    }
}
