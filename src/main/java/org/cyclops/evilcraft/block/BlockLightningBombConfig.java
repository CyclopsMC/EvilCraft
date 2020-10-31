package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockLightningBomb}.
 * @author rubensworks
 *
 */
public class BlockLightningBombConfig extends BlockConfig {

    public BlockLightningBombConfig() {
        super(
                EvilCraft._instance,
            "lightning_bomb",
                eConfig -> new BlockLightningBomb(Block.Properties.create(Material.TNT)
                        .hardnessAndResistance(0.0F)
                        .sound(SoundType.GROUND)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
}
