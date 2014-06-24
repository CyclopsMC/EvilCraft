package evilcraft.modcompat.fmp;

import java.util.List;

import net.minecraft.block.Block;
import codechicken.microblock.BlockMicroMaterial;

import com.google.common.collect.Lists;

/**
 * API handler for Forge Multipart.
 * @author rubensworks
 *
 */
public class ForgeMultipart {
	
	private static List<Block> blocks = Lists.newLinkedList();
    
    /**
     * Register a block as a multipart block.
     * @param block The block to be microblock registered.
     */
    public static void registerBlock(Block block) {
    	blocks.add(block);
    }
    
    /**
     * Start the actual registration of the blocks as multiparts.
     * This can only be called from the init or later.
     */
    public static void actualRegisterBlocks() {
    	for(Block block : blocks) {
    		BlockMicroMaterial.createAndRegister(block, 0);
    	}
    }

}
