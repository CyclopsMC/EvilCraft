package evilcraft;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Listener that can be given to the {@link EvilCraft} instance.
 * @author rubensworks
 *
 */
public interface IInitListener {

	/**
     * This will be called in an init step.
	 * @param initStep The init step.
     */
    public void onInit(Step initStep);
    
    /**
     * The possible init steps.
     * @author rubensworks
     *
     */
    public static enum Step {
    	
    	/**
    	 * Mod pre-init, {@link FMLPreInitializationEvent}
    	 */
    	PREINIT,
    	/**
    	 * Mod init, {@link FMLInitializationEvent}
    	 */
    	INIT,
    	/**
    	 * Mod post-init, {@link FMLPostInitializationEvent}.
    	 */
    	POSTINIT;
    	
    }
	
}
