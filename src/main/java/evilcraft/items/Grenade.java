package evilcraft.items;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import evilcraft.Reference;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.ItemConfig;
import evilcraft.api.config.configurable.ConfigurableItem;
import evilcraft.api.item.grenades.IGrenadeType;
import evilcraft.api.item.grenades.LightningGrenadeType;
import evilcraft.api.item.grenades.RedstoneGrenadeType;
import evilcraft.entities.item.EntityGrenade;

/**
 * Represents an EvilCraft grenade.
 * @author immortaleeb
 *
 */
public class Grenade extends ConfigurableItem {
	/**
	 * The id of the object in the DataWatcher object of {@link EntityGrenade} that
	 * is used to save the grenade types that are present in the entity.
	 */
	public static final int DATA_WATCHER_TYPES_ID = 22;
	private static final String NBT_TYPE_TAG = "grenadeTypes";
	private static final ResourceLocation defaultTextureLocation = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ITEMS + "grenade.png");
	
    private static final IGrenadeType[] GRENADE_TYPES = {
            RedstoneGrenadeType.getInstance(),
            LightningGrenadeType.getInstance()
    };

    private static Grenade _instance = null;

    /**
     * Initialise the configurable.
     * @param eConfig The config.
     */
    public static void initInstance(ExtendedConfig<ItemConfig> eConfig) {
        if(_instance == null)
            _instance = new Grenade(eConfig);
        else
            eConfig.showDoubleInitError();
    }

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static Grenade getInstance() {
        return _instance;
    }

    /**
     * Creates a new instance.
     * @param eConfig The extended config for this item.
     */
    public Grenade(ExtendedConfig<ItemConfig> eConfig) {
        super(eConfig);
        this.maxStackSize = 16;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if(!world.isRemote) {
            if (!entityPlayer.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
            world.playSoundAtEntity(entityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            // Spawn a new entity on both client and server side
            EntityThrowable grenade = new EntityGrenade(world, entityPlayer);
            world.spawnEntityInWorld(grenade);

            // Update the entity's types on both client and server side
            DataWatcher dw = grenade.getDataWatcher();
            // FIXME no need to deserialize and then serialize again, just keep the special cases (e.g. null) in mind 
            dw.updateObject(Grenade.DATA_WATCHER_TYPES_ID, serializeGrenadeTypes(deserializeGrenadeTypes((itemStack))));
        }
        return itemStack;
    }
    
    @SuppressWarnings({"rawtypes"})
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        super.addInformation(itemStack, entityPlayer, list, par4);

        List<IGrenadeType> grenadeTypes = deserializeGrenadeTypes(itemStack);
        for (IGrenadeType type : grenadeTypes) {
            type.addInformation(list);
        }
    }

    @SuppressWarnings({"rawtypes"})
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List itemList) {
    	// Regular plain grenade
    	itemList.add(new ItemStack(item, 1));
    	
    	// Each of the different grenade types
        for (int i = 0; i < GRENADE_TYPES.length; ++i) {
        	IGrenadeType grenadeType = GRENADE_TYPES[i];
        	ItemStack itemStack = new ItemStack(item, 1);
            
        	serializeGrenadeType(grenadeType, itemStack);
        	itemList.add(itemStack);
        }
    }
    
    @Override
    public void registerIcons(IIconRegister iconRegister) {
    	super.registerIcons(iconRegister);
    	
    	for (IGrenadeType grenadeType : GRENADE_TYPES)
    		grenadeType.registerIcons(iconRegister);
    }
    
    /**
     * Returns the texture location for the texture of this grenade.
     * @param stack An item stack that contains the grenade for which we need the texture.
     * @return The location of the texture for the given item stack.
     */
    public ResourceLocation getTextureLocation(ItemStack stack) {
    	IGrenadeType grenadeType = getLastGrenadeType(stack);
    	
    	return grenadeType == null ? defaultTextureLocation : grenadeType.getTextureLocation();
    }
    
    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
    	IGrenadeType grenadeType = getLastGrenadeType(stack);
    	
    	return grenadeType == null ? super.getIcon(stack, pass) : grenadeType.getIcon();
    }
    
    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player,
    		ItemStack usingItem, int useRemaining) {
    	return getIcon(stack, renderPass);
    }
    
    private IGrenadeType getLastGrenadeType(ItemStack stack) {
    	List<IGrenadeType> grenadeTypes = deserializeGrenadeTypes(stack);
    	
    	if (!grenadeTypes.isEmpty())
    		return grenadeTypes.get(grenadeTypes.size()-1);
    	
    	return null;
    }
    
    /**
     * Serializes the given grenade type by saving it in NBT into the given item stack.
     * @param type The grenade type that should be serialized.
     * @param itemStack The item stack in which we should save the grenade type.
     */
    public static void serializeGrenadeType(IGrenadeType type, ItemStack itemStack) {
    	List<IGrenadeType> list = new LinkedList<IGrenadeType>();
    	list.add(type);
    	serializeGrenadeTypes(list, itemStack);
    }
    
    /**
     * Serializes the given list of grenade types into the given item stack through NBT.
     * @param types A list of grenade types that should be serialized.
     * @param itemStack The item stack in which we should save the grenade types.
     */
    public static void serializeGrenadeTypes(List<IGrenadeType> types, ItemStack itemStack) {
    	NBTTagCompound compound = itemStack.stackTagCompound;
    	if (compound == null) {
    		compound = new NBTTagCompound();
    		itemStack.setTagCompound(compound);
    	}
    	serializeGrenadeTypes(types, compound);
    }
    
    /**
     * Serializes the given list of grenade types into the given NBT tag compound.
     * @param types A list of grenade types that should be serialized.
     * @param compound The NBT tag compound in which we should save the grenade types.
     */
    public static void serializeGrenadeTypes(List<IGrenadeType> types, NBTTagCompound compound) {
    	compound.setInteger(NBT_TYPE_TAG, serializeGrenadeTypes(types));
    }

    /**
     * Serializes the given list of grenade types by storing them into a single integer.
     * @param types A list of grenade types that should be serialized.
     * @return An integer, which is the serialized form of the given grenade types.
     */
    public static int serializeGrenadeTypes(List<IGrenadeType> types) {
        int result = 0;

        for (IGrenadeType type : types)
            result |= type.getId();

        return result;
    }
    
    /**
     * Deserializes a list of grenade types from the given item stack by looking into the
     * NBT associated with the item stack.
     * @param itemStack An item stack containing information about the grenade types that need to be deserialized.
     * @return A list of grenade types that were serialized inside the given item stack.
     */
    public static List<IGrenadeType> deserializeGrenadeTypes(ItemStack itemStack) {
    	List<IGrenadeType> result = null;
    	
    	if (itemStack.stackTagCompound == null) {
    		result = new LinkedList<IGrenadeType>();
    	} else {
    		result = deserializeGrenadeTypes(itemStack.stackTagCompound);
    	}
    	
    	return result;
    }
    
    /**
     * Deserializes a list of grenade types from a given NBT tag compound.
     * @param compound An NBT tag compound containing information about the grenade types. 
     * @return A list of grenade types that are serialized inside the given NBT tag compounds.
     */
    public static List<IGrenadeType> deserializeGrenadeTypes(NBTTagCompound compound) {
    	return deserializeGrenadeTypes(compound.getInteger(NBT_TYPE_TAG));
    }

    /**
     * Deserializes a list of grenade types from a given integer that contains the serialized
     * form of these grenade types.
     * @param serializedTypes An integer that contains the serialized grenade types.
     * @return A list of grenade types that are serialized inside the given integer.
     */
    public static List<IGrenadeType> deserializeGrenadeTypes(int serializedTypes) {
        List<IGrenadeType> types = new LinkedList<IGrenadeType>();
        
        if (serializedTypes == 0) 
        	return types;

        for (IGrenadeType type : GRENADE_TYPES) {
            if ((serializedTypes & type.getId()) != 0)
                types.add(type);
        }

        return types;
    }
}
