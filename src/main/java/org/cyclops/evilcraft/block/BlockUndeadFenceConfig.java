package org.cyclops.evilcraft.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Undead Fence.
 * @author rubensworks
 *
 */
public class BlockUndeadFenceConfig extends BlockConfig {

    public BlockUndeadFenceConfig() {
        super(
                EvilCraft._instance,
            "undead_fence",
                eConfig -> new FenceBlock(AbstractBlock.Properties.create(Material.WOOD, MaterialColor.ORANGE_TERRACOTTA)
                        .hardnessAndResistance(2.0F, 3.0F)
                        .sound(SoundType.WOOD)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        BlockHelpers.setFireInfo(getInstance(), 5, 20);
    }
    
}
