package evilcraft.core.config.configurable.propertycallback;

import evilcraft.core.config.configurable.ConfigurableProperty;

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
	
	/**
	 * Called at post-init when this property is active.
	 * @param value The property value.
	 */
	public void onRegisteredPostInit(Object value);
	
}
