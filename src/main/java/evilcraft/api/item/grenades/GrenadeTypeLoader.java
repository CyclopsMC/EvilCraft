package evilcraft.api.item.grenades;

import evilcraft.IInitListener;

/**
 * This class loads the different grenade types and makes sure that
 * they are saved correctly after registration.
 * @author immortaleeb
 *
 */
public class GrenadeTypeLoader implements IInitListener {

	@Override
	public void onInit(Step initStep) {
		switch(initStep) {
		case INIT:
			onInit();
			break;
		case POSTINIT:
			onPostInit();
			break;
		default:
		}
	}
	
	private void onInit() {
		GrenadeTypeRegistry registry = GrenadeTypeRegistry.getInstance();
		registry.registerGrenadeType(LightningGrenadeType.UNIQUE_NAME, LightningGrenadeType.class);
		registry.registerGrenadeType(RedstoneGrenadeType.UNIQUE_NAME, RedstoneGrenadeType.class);
	}
	
	private void onPostInit() {
		GrenadeTypeRegistry.getInstance().afterRegistration();
	}

}
