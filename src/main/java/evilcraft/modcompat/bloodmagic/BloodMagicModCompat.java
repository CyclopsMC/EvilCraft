package evilcraft.modcompat.bloodmagic;

import cpw.mods.fml.common.FMLCommonHandler;
import evilcraft.Configs;
import evilcraft.IInitListener;
import evilcraft.Reference;
import evilcraft.modcompat.IModCompat;
import evilcraft.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;

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
	        Configs.getInstance().configs.add(new BoundBloodDropConfig());
    	} else if(step == IInitListener.Step.INIT) {
    		FMLCommonHandler.instance().bus().register(ClientSoulNetworkHandler.getInstance());
    		MinecraftForge.EVENT_BUS.register(ClientSoulNetworkHandler.getInstance());
	        PacketHandler.register(UpdateSoulNetworkCachePacket.class);
	        PacketHandler.register(RequestSoulNetworkUpdatesPacket.class);
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
