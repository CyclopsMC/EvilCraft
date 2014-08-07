package evilcraft.api.config.configurable.propertycallback;

import evilcraft.api.config.configurable.ConfigurableProperty;

/**
 * A callback for when a {@link ConfigurableProperty} has been changed.
 * @author rubensworks
 *
 */
public interface IChangedCallback {

	/**
	 * Called when this property has been changed.
	 * @param value The new property value.
	 */
	public void onChanged(Object value);
	
}
