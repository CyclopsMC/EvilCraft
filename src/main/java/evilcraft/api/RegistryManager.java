package evilcraft.api;

import java.util.Map;

import com.google.common.collect.Maps;

import evilcraft.api.degradation.IDegradationRegistry;
import evilcraft.api.recipes.custom.ISuperRecipeRegistry;
import evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import evilcraft.core.degradation.DegradationRegistry;
import evilcraft.core.recipe.custom.SuperRecipeRegistry;
import evilcraft.tileentity.tickaction.bloodchest.BloodChestRepairActionRegistry;

/**
 * Manager for all the registries in this mod.
 * @author rubensworks
 *
 */
public class RegistryManager {

	private static RegistryManager _instance = null;
	
	private Map<Class<? extends IRegistry>, IRegistry> registries;
	
	private RegistryManager() {
		registries = Maps.newHashMap();
		addRegistry(IDegradationRegistry.class, new DegradationRegistry());
		addRegistry(ISuperRecipeRegistry.class, new SuperRecipeRegistry());
		addRegistry(IBloodChestRepairActionRegistry.class, new BloodChestRepairActionRegistry());
	}
	
	private void addRegistry(Class<? extends IRegistry> clazz, IRegistry registry) {
		registries.put(clazz, registry);
	}
	
	/**
	 * Get the unique registry of the given class.
	 * @param clazz The class of the registry.
	 * @return The unique registry.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IRegistry> T getRegistry(Class<T> clazz) {
		return (T) RegistryManager.getInstance().registries.get(clazz);
	}
	
	/**
	 * Get the unique instance of the registry manager.
	 * @return The unique instance.
	 */
	public static RegistryManager getInstance() {
		if(_instance == null) {
			_instance = new RegistryManager();
		}
		return _instance;
	}
	
}
