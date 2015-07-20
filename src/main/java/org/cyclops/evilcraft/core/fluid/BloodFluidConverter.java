package org.cyclops.evilcraft.core.fluid;

import org.cyclops.cyclopscore.config.IChangedCallback;
import org.cyclops.evilcraft.fluid.Blood;

/**
 * An implicit blood converter.
 * @author rubensworks
 *
 */
public class BloodFluidConverter extends ImplicitFluidConverter {

	private static BloodFluidConverter _instance = new BloodFluidConverter();
	
	/**
	 * Get the unique instance.
	 * @return The instance.
	 */
	public static BloodFluidConverter getInstance() {
		if(_instance == null) {
			_instance = new BloodFluidConverter();
		}
		if(_instance.getTarget() == null) {
			_instance.resetTarget();
		}
		return _instance;
	}
	
	private BloodFluidConverter() {
		super(Blood.getInstance());
	}
	
	protected void resetTarget() {
		setTarget(Blood.getInstance());
	}
	
	/**
	 * Callback for when the blood converters property is changed.
	 * @author rubensworks
	 *
	 */
	public static class BloodConvertersChanged implements IChangedCallback {

		private static boolean calledOnce = false;
		
		@Override
		public void onChanged(Object value) {
			if(calledOnce) {
				BloodFluidConverter.getInstance().registerFromConfig((String[]) value);
			}
			calledOnce = true;
		}

		@Override
		public void onRegisteredPostInit(Object value) {
			onChanged(value);
		}
		
	}

}
