package evilcraft.api.config;

import java.lang.reflect.Field;

import evilcraft.api.config.configurable.propertycallback.IChangedCallback;

/**
 * A helper class to contain callbacks for when the config file has been read
 * so that set values can be updated in the config objects.
 * @author rubensworks
 *
 */
public class ConfigPropertyCallback {
	
	protected IChangedCallback changedCallback;
	protected Field field;
	
	/**
	 * Make a new instance.
	 * @param changedCallback The additional optional callback.
	 * @param field 
	 */
	public ConfigPropertyCallback(IChangedCallback changedCallback, Field field) {
		this.changedCallback = changedCallback;
		this.field = field;
	}
	
    /**
     * Called when a config is updated.
     * @param newValue The new value of this config property.
     */
    public void run(Object newValue) {
    	try {
            field.set(null, newValue);
        } catch (IllegalArgumentException e1) {
            // Shouldn't be possible
            e1.printStackTrace();
        } catch (IllegalAccessException e2) {
        	e2.printStackTrace();
        }
        if(changedCallback != null) {
        	changedCallback.onChanged(newValue);
        }
    }

	/**
	 * @return the changedCallback
	 */
	protected IChangedCallback getChangedCallback() {
		return changedCallback;
	}
}
