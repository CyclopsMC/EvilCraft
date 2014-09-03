package evilcraft.api.item.grenades;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import scala.actors.threadpool.Arrays;

/**
 * Registry responsible for adding new grenade types.
 * @author immortaleeb
 *
 */
public class GrenadeTypeRegistry {
	
	private static final String UNIQUE_IDS_FILE = "config/evilcraft/grenade_type_ids.txt";
	
	private static GrenadeTypeRegistry _instance = null;
	
	/**
	 * @return Returns the unique instance of this class.
	 */
	public static GrenadeTypeRegistry getInstance() {
		if (_instance == null)
			_instance = new GrenadeTypeRegistry();
		return _instance;
	}
	
	private Map<String, IGrenadeType> grenadeTypes;
	private Map<String, Integer> uniqueIds;
	private boolean registrationClosed;
	private boolean fileChanged;
	
	private GrenadeTypeRegistry() {
		registrationClosed = false;
		fileChanged = false;
		
		grenadeTypes = new HashMap<String, IGrenadeType>();
		uniqueIds = new HashMap<String, Integer>();
		
		readIdsFromFile();
	}
	
	private void readIdsFromFile() {
		File file = new File(UNIQUE_IDS_FILE);
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {
				String key = sc.next();
				int val = sc.nextInt();
				uniqueIds.put(key, val);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				throw new RuntimeException("Failed to create unique grenade type ids file at " + UNIQUE_IDS_FILE);
			}
		}
	}
	
	private int getNextId() {
		Object[] arr = uniqueIds.values().toArray();
		Arrays.sort(arr);
		
		int i = 1;
		while (i <= arr.length) {
			Integer val = ((Integer)(arr[i-1]));
			if (val.intValue() > i)
				return i;
			i++;
		}
		
		return i;
	}
	
	/**
	 * Registers a new grenade type.
	 * @param uniqueName A unique name to identify the given grenade type.
	 * @param type The class of the grenade type that should be registered. Note that
	 *                    this class MUST implement a constructor that takes an id of type int
	 *                    as parameter.
	 * @return Returns an instance of the given grenade type.
	 */
	public IGrenadeType registerGrenadeType(String uniqueName, Class<? extends IGrenadeType> type) {
		if (registrationClosed)
			throw new RuntimeException("You are not allowed to register any more grenadeTypeS after afterRegistration() has been called");
		// Check if we need to generate a new id for the given unique name
		// Some ids might be preloaded from a file, we don't need to generate a new id in that case
		Integer id = uniqueIds.get(uniqueName);
		if (id == null) {
			id = getNextId();
			uniqueIds.put(uniqueName, id);
			fileChanged = true;
		}
		
		// Create a new instance of the grenade type and give it its id.
		IGrenadeType grenadeType = null;
		Constructor<? extends IGrenadeType> constructor;
		try {
			constructor = type.getConstructor(int.class);
			constructor.setAccessible(true);
			grenadeType = constructor.newInstance(id);
			grenadeTypes.put(uniqueName, grenadeType);
		} catch (Exception e) {
			throw new RuntimeException("Could not register a grenade type of type " + type + ", make sure it has a constructor that takes an id"
					+ "and that the constructor is accesible"); 
		}
		
		// Register all recipes
		grenadeType.registerCraftingRecipes();
		
		return grenadeType;
	}
	
	/**
	 * Returns an instance of the grenade type with the given unique name.
	 * @param uniqueName The unique name of the grenade type.
	 * @return The grenade type, or null in case no grenade type matches the given name.
	 */
	public IGrenadeType getGrenadeType(String uniqueName) {
		return grenadeTypes.get(uniqueName);
	}
	
	/**
	 * @return Returns a collection of all registered grenade types.
	 */
	public Collection<IGrenadeType> getGrenadeTypes() {
		return grenadeTypes.values();
	}
	
	/**
	 * Returns the unique id for the given grenade type.
	 * @param grenadeType Grenade type for which we want the unique id.
	 * @return A unique id (integer) for the given grenade type.
	 */
	public int getUniqueId(IGrenadeType grenadeType) {
		Integer result = uniqueIds.get(grenadeType);
		return  result == null ? 0 : result; 
	}
	
	/**
	 * @return Returns all unique ids of all registered grenade types as a map
	 * that maps the unique name of the grenade type to its unique id.
	 */
	public Map<String, Integer> getUniqueIds() {
		return uniqueIds;
	}
	
	/**
	 * Call this after all grenade types are registered. This will close the registration
	 * and save all unique ids in a file for later use. This allows us to update the client
	 * and register new grenade types later without breaking existing worlds/mixing existing
	 * grenade types.
	 */
	public void afterRegistration() {
		if (fileChanged) {
			File file = new File(UNIQUE_IDS_FILE);
			PrintWriter writer;
			try {
				writer = new PrintWriter(file);
				for (Entry<String, Integer> entry : uniqueIds.entrySet()) {
					writer.print(entry.getKey());
					writer.print(" ");
					writer.println(entry.getValue());
				}
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		registrationClosed = true;
	}
}
