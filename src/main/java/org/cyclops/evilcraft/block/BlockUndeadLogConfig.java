package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
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
                eConfig -> new LogBlock(MaterialColor.ORANGE_TERRACOTTA, Block.Properties.create(Material.WOOD)
                        .hardnessAndResistance(2.0F)
                        .sound(SoundType.WOOD)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

    /* TODO
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_WOODLOG;
    } */
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ((FireBlock) Blocks.FIRE).setFireInfo(getInstance(), 5, 20);
    }
    
}
