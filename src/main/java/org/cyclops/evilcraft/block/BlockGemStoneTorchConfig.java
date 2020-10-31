package org.cyclops.evilcraft.block;


import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.Material;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.evilcraft.EvilCraft;

/**
 * Config for the Burning Gemstone Torch.
 * @author rubensworks
 *
 */
public class BlockGemStoneTorchConfig extends BlockConfig {

    @ConfigurableProperty(category = "block", comment = "The radius that will be kept spirit-proof.", isCommandable = true)
    public static int area = 15;

    public BlockGemStoneTorchConfig() {
        super(
                EvilCraft._instance,
            "gem_stone_torch",
                eConfig -> new TorchBlock(Block.Properties.create(Material.MISCELLANEOUS)
                        .doesNotBlockMovement()
                        .hardnessAndResistance(0)
                        .lightValue(14)
                        .sound(SoundType.WOOD)) {

                },
                getDefaultItemConstructor(EvilCraft._instance)
        );
    }
    
}
