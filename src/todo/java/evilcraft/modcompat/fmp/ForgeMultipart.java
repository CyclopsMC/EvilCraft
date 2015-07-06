package evilcraft.modcompat.fmp;

import codechicken.microblock.BlockMicroMaterial;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;

import java.util.List;

/**
 * API handler for Forge Multipart.
 * @author rubensworks
 *
 */
public class ForgeMultipart {
	
	private static List<Block> blocks = Lists.newLinkedList();
    
    /**
     * Register a blockState as a multipart blockState.
     * @param block The blockState to be microblock registered.
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
