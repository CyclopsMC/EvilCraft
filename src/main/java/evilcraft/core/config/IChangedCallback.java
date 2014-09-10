package evilcraft.core.config;


/**
 * A callback for when a {@link ConfigurableProperty} has been changed.
 * Classes implementing this interface can be given to {@link ConfigurableProperty}
 * as a {@link ConfigurableProperty#changedCallback()} field. When that is done, the
 * given class MUST have a default public constructor.
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
