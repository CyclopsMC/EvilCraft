package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the Reinforced Undead Plank Stairs.
 * @author rubensworks
 *
 */
public class BlockReinforcedUndeadPlankStairsConfig extends BlockConfigCommon<IModBase> {

    public BlockReinforcedUndeadPlankStairsConfig() {
        super(
                EvilCraft._instance,
                "reinforced_undead_planks_stairs",
                (eConfig, properties) -> new StairBlock(RegistryEntries.BLOCK_REINFORCED_UNDEAD_PLANKS.get().defaultBlockState(),
                        properties
                                .strength(1.5F)
                                .sound(SoundType.WOOD)
                                .isValidSpawn((state, level, pos, type) -> false)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
