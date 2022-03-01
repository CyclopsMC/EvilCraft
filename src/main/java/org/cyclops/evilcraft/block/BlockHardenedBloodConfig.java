package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * A config for {@link BlockHardenedBlood}.
 * @author rubensworks
 *
 */
public class BlockHardenedBloodConfig extends BlockConfig {

    public BlockHardenedBloodConfig() {
        super(
                EvilCraft._instance,
            "hardened_blood",
                eConfig -> new BlockHardenedBlood(Block.Properties.of(Material.ICE)
                        .strength(0.5F)
                        .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
