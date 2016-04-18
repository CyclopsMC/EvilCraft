package org.cyclops.evilcraft.modcompat.bloodmagic;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cyclops.cyclopscore.init.IInitListener;
import org.cyclops.cyclopscore.modcompat.IModCompat;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.Reference;

/**
 * Compatibility plugin for Blood Magic.
 * @author rubensworks
 *
 */
public class BloodMagicModCompat implements IModCompat {

    @Override
    public String getModID() {
       return Reference.MOD_BLOODMAGIC;
    }

    @Override
    public void onInit(IInitListener.Step step) {
    	if(step == IInitListener.Step.PREINIT) {
    		ClientSoulNetworkHandler.reset();
			EvilCraft._instance.getConfigHandler().add(new BoundBloodDropConfig());
    	} else if(step == IInitListener.Step.INIT) {
    		FMLCommonHandler.instance().bus().register(ClientSoulNetworkHandler.getInstance());
    		MinecraftForge.EVENT_BUS.register(ClientSoulNetworkHandler.getInstance());
    	} else if(step == IInitListener.Step.POSTINIT) {
			EvilCraft._instance.getPacketHandler().register(UpdateSoulNetworkCachePacket.class);
			EvilCraft._instance.getPacketHandler().register(RequestSoulNetworkUpdatesPacket.class);
		}
    }
    
    @Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getComment() {
		return "Bound Blood Drop item to directly drain Blood from your soul network.";
	}

}
