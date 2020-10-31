package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the {@link BlockUndeadLeaves}.
 * @author rubensworks
 *
 */
public class BlockUndeadLeavesConfig extends BlockConfig {

    public BlockUndeadLeavesConfig() {
        super(
                EvilCraft._instance,
            "undead_leaves",
                eConfig -> new BlockUndeadLeaves(Block.Properties.create(Material.LEAVES)
                        .hardnessAndResistance(0.5F)
                        .sound(SoundType.GROUND)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ((FireBlock) Blocks.FIRE).setFireInfo(getInstance(), 30, 60);
    }

    /* TODO
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_LEAVESTREE;
    }*/
}
