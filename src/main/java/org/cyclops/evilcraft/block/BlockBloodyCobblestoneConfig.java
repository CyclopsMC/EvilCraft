package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Bloody Cobblestone.
 * @author rubensworks
 *
 */
public class BlockBloodyCobblestoneConfig extends BlockConfig {

    public BlockBloodyCobblestoneConfig() {
        super(
            EvilCraft._instance,
            "bloody_cobblestone",
                eConfig -> new Block(Block.Properties.of(Material.STONE)
                        .strength(1.5F, 10.0F)
                        .sound(SoundType.STONE)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(0)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
}
