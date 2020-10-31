package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
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
                eConfig -> new BlockHardenedBlood(Block.Properties.create(Material.ICE)
                        .hardnessAndResistance(0.5F)
                        .sound(SoundType.STONE)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(0)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
