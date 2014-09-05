package evilcraft.modcompat.buildcraft;

import net.minecraft.block.Block;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;
import evilcraft.Reference;
import evilcraft.core.config.BlockConfig;

/**
 * Buildcraft helper class for registering facade blocks.
 * @author rubensworks
 *
 */
public class BuildcraftHelper {

    /**
     * Register a block config as facade.
     * @param eConfig The block config.
     */
    public static void registerFacade(BlockConfig eConfig) {
        if(Loader.isModLoaded(Reference.MOD_BUILDCRAFT_TRANSPORT)) {
            FMLInterModComms.sendMessage(Reference.MOD_BUILDCRAFT_TRANSPORT, "add-facade", Block.blockRegistry.getNameForObject((Block)eConfig.getSubInstance())+"@"+0);
        }
    }
    
}
