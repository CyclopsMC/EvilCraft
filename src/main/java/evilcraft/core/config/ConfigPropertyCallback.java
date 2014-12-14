package evilcraft.core.config;

/**
 * A helper class to contain callbacks for when the config file has been read
 * so that set values can be updated in the config objects.
 * @author rubensworks
 *
 */
public final class ConfigPropertyCallback {
	
	protected IChangedCallback changedCallback;
	protected ConfigProperty property;
	
	/**
	 * Make a new instance.
	 * @param changedCallback The additional optional callback.
	 */
	public ConfigPropertyCallback(IChangedCallback changedCallback) {
		this.changedCallback = changedCallback;
	}

    /**
     * @param property The config property
     */
    public void setConfigProperty(ConfigProperty property) {
        this.property = property;
    }
	
    /**
     * Called when a config is updated.
     * @param newValue The new value of this config property.
     */
    public void run(Object newValue) {
        property.setValue(newValue);
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
