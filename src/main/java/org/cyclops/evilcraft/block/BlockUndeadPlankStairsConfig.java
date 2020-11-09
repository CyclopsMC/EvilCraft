package org.cyclops.evilcraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.RegistryEntries;

/**
 * Config for the Undead Plank Stairs.
 * @author rubensworks
 *
 */
public class BlockUndeadPlankStairsConfig extends BlockConfig {

    public BlockUndeadPlankStairsConfig() {
        super(
                EvilCraft._instance,
                "undead_planks_stairs",
                eConfig -> new StairsBlock(() -> RegistryEntries.BLOCK_DARK_BLOOD_BRICK.getDefaultState(),
                        Block.Properties.create(Material.WOOD)
                                .hardnessAndResistance(2.0F)
                                .sound(SoundType.WOOD)),
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }

    /* TODO
    @Override
    public String getOreDictionaryId() {
        return Reference.DICT_STAIRWOOD;
    }*/

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ((FireBlock) Blocks.FIRE).setFireInfo(getInstance(), 5, 20);
    }

}
