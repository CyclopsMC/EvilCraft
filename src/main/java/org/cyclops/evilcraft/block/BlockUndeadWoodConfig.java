package org.cyclops.evilcraft.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Undead Wood.
 * @author rubensworks
 *
 */
public class BlockUndeadWoodConfig extends BlockConfig {

    public BlockUndeadWoodConfig() {
        super(
                EvilCraft._instance,
            "undead_wood",
                eConfig -> Blocks.createLogBlock(MaterialColor.ORANGE_TERRACOTTA, MaterialColor.ORANGE_TERRACOTTA),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        BlockHelpers.setFireInfo(getInstance(), 5, 20);
    }
    
}
