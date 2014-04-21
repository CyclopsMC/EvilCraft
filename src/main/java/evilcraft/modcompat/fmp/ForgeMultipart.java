package evilcraft.modcompat.fmp;

import net.minecraft.block.Block;
import codechicken.microblock.BlockMicroMaterial;

/**
 * API handler for Forge Multipart.
 * @author rubensworks
 *
 */
public class ForgeMultipart {
    
    /**
     * Register a block as a multipart block.
     * @param block The block to be microblock registered.
     */
    public static void registerBlock(Block block) {
        BlockMicroMaterial.createAndRegister(block, 0);
    }

}
