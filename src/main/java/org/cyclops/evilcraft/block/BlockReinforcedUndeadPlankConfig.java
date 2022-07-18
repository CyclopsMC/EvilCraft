package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockReinforcedUndeadPlank}.
 * @author rubensworks
 *
 */
public class BlockReinforcedUndeadPlankConfig extends BlockConfig {

    public BlockReinforcedUndeadPlankConfig() {
        super(
                EvilCraft._instance,
            "reinforced_undead_planks",
                eConfig -> new BlockReinforcedUndeadPlank(Block.Properties.of(Material.STONE)
                        .strength(5.0F)
                        .sound(SoundType.WOOD)
                        .noOcclusion()),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
