package evilcraft.modcompat.fmp;

import evilcraft.Reference;
import evilcraft.core.config.extendedconfig.BlockConfig;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Loader;

/**
 * FMP helper class for registering microblock.
 * @author rubensworks
 *
 */
public class ForgeMultipartHelper {

    /**
     * Register a blockState config as microblock.
     * @param eConfig The blockState config.
     */
    public static void registerMicroblock(BlockConfig eConfig) {
        if(Loader.isModLoaded(Reference.MOD_FMP)) {
            ForgeMultipart.registerBlock((Block) eConfig.getSubInstance());
        }
    }
    
}
