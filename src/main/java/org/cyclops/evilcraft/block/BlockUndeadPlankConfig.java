package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Undead Plank.
 * @author rubensworks
 *
 */
public class BlockUndeadPlankConfig extends BlockConfig {

    public BlockUndeadPlankConfig() {
        super(
                EvilCraft._instance,
            "undead_plank",
                eConfig -> new Block(Block.Properties.create(Material.WOOD)
                        .hardnessAndResistance(2.0F)
                        .sound(SoundType.WOOD)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

    /* TODO
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_WOODPLANK;
    }*/
    
    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ((FireBlock) Blocks.FIRE).setFireInfo(getInstance(), 5, 20);
    }
    
}
