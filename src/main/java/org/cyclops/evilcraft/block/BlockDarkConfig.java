package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the dark block.
 * @author rubensworks
 *
 */
public class BlockDarkConfig extends BlockConfig {

    public BlockDarkConfig() {
        super(
                EvilCraft._instance,
            "dark_block",
                eConfig -> new Block(Block.Properties.of(Material.STONE)
                        .strength(5.0F)
                        .sound(SoundType.METAL)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(2)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
}
