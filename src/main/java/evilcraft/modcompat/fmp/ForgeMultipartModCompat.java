package evilcraft.modcompat.fmp;

import evilcraft.Reference;
import evilcraft.modcompat.IModCompat;

/**
 * Compatibility plugin for Forestry.
 * @author rubensworks
 *
 */
public class ForgeMultipartModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_FMP;
    }

    @Override
    public void preInit() {
        
    }

    @Override
    public void init() {
        ForgeMultipart.actualRegisterBlocks();
    }

    @Override
    public void postInit() {
        
    }

}
