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
    public void onInit(IInitListener.Step step) {
        if(step == IInitListener.Step.INIT) {
        	ForgeMultipart.actualRegisterBlocks();
        }
    }
    
    @Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Multipart and microblock support.";
	}

}
