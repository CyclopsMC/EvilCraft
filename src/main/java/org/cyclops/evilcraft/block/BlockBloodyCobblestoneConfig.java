package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
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
                eConfig -> new Block(Block.Properties.of()
                        .requiresCorrectToolForDrops()
                        .strength(1.5F, 10.0F)
                        .sound(SoundType.STONE)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

}
