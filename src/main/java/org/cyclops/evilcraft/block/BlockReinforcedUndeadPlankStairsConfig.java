package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the Reinforced Undead Plank Stairs.
 * @author rubensworks
 *
 */
public class BlockReinforcedUndeadPlankStairsConfig extends BlockConfig {

    public BlockReinforcedUndeadPlankStairsConfig() {
        super(
                EvilCraft._instance,
                "reinforced_undead_planks_stairs",
                eConfig -> new StairBlock(() -> RegistryEntries.BLOCK_REINFORCED_UNDEAD_PLANKS.defaultBlockState(),
                        Block.Properties.of()
                                .strength(1.5F)
                                .sound(SoundType.WOOD)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
