package org.cyclops.evilcraft.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.material.Material;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
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
                eConfig -> new StairBlock(() -> RegistryEntries.BLOCK_DARK_BLOOD_BRICK.defaultBlockState(),
                        Block.Properties.of(Material.WOOD)
                                .strength(2.0F)
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
