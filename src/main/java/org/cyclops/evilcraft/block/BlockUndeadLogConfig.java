package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Undead Log.
 * @author rubensworks
 *
 */
public class BlockUndeadLogConfig extends BlockConfig {

    public BlockUndeadLogConfig() {
        super(
                EvilCraft._instance,
            "undead_log",
                eConfig -> Blocks.log(MaterialColor.TERRACOTTA_ORANGE, MaterialColor.TERRACOTTA_ORANGE),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        BlockHelpers.setFireInfo(getInstance(), 5, 20);
    }
    
}
