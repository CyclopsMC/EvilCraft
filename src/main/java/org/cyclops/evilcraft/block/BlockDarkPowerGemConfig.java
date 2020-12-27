package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the dark power gem block.
 * @author rubensworks
 *
 */
public class BlockDarkPowerGemConfig extends BlockConfig {

    public BlockDarkPowerGemConfig() {
        super(
                EvilCraft._instance,
            "dark_power_gem_block",
                eConfig -> new Block(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(5.0F)
                        .sound(SoundType.METAL)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(2)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
}
