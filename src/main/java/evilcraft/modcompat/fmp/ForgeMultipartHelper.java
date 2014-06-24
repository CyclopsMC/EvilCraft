package evilcraft.modcompat.fmp;

import net.minecraft.block.Block;
import cpw.mods.fml.common.Loader;
import evilcraft.Reference;
import evilcraft.api.config.BlockConfig;

/**
 * FMP helper class for registering microblock.
 * @author rubensworks
 *
 */
public class ForgeMultipartHelper {

    /**
     * Register a block config as microblock.
     * @param eConfig The block config.
     */
    public static void registerMicroblock(BlockConfig eConfig) {
        if(Loader.isModLoaded(Reference.MOD_FMP)) {
            ForgeMultipart.registerBlock((Block) eConfig.getSubInstance());
        }
    }
    
}
