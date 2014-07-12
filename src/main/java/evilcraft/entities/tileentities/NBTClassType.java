package evilcraft.entities.tileentities;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import evilcraft.api.entities.tileentitites.EvilCraftTileEntity;

/**
 * Types of NBT field classes used for persistence of fields in {@link EvilCraftTileEntity}.
 * @author rubensworks
 *
 * @param <T> The field class type.
 * @see NBTPersist
 */
public abstract class NBTClassType<T> {
    
    /**
     * A map of all the types to their persist actions.
     */
    public static Map<Class<?>, NBTClassType<?>> NBTYPES = new HashMap<Class<?>, NBTClassType<?>>(); 
    static {
        NBTYPES.put(Integer.class, new NBTClassType<Integer>() {

            @Override
            protected void writePersistedField(String name, Integer object, NBTTagCompound tag) {
                tag.setInteger(name, object);
            }

            @Override
            protected Integer readPersistedField(String name, NBTTagCompound tag) {
                return tag.getInteger(name);
            }
        });
        
        NBTYPES.put(Float.class, new NBTClassType<Float>() {

            @Override
            protected void writePersistedField(String name, Float object, NBTTagCompound tag) {
                tag.setFloat(name, object);
            }

            @Override
            protected Float readPersistedField(String name, NBTTagCompound tag) {
                return tag.getFloat(name);
            }
        });
        
        NBTYPES.put(Boolean.class, new NBTClassType<Boolean>() {

            @Override
            protected void writePersistedField(String name, Boolean object, NBTTagCompound tag) {
                tag.setBoolean(name, object);
            }

            @Override
            protected Boolean readPersistedField(String name, NBTTagCompound tag) {
                return tag.getBoolean(name);
            }
        });
        
        NBTYPES.put(NBTTagCompound.class, new NBTClassType<NBTTagCompound>() {

            @Override
            protected void writePersistedField(String name, NBTTagCompound object, NBTTagCompound tag) {
                tag.setTag(name, object);
            }

            @Override
            protected NBTTagCompound readPersistedField(String name, NBTTagCompound tag) {
                return tag.getCompoundTag(name);
            }
        });
    }
    
    /**
     * Perform a field persist action.
     * @param tile The tile entity that has the field.
     * @param field The field to persist or read.
     * @param tag The tag compound to read or write to.
     * @param write If there should be written, otherwise there will be read.
     */
    public static void performActionForField(EvilCraftTileEntity tile, Field field, NBTTagCompound tag, boolean write) {
        Class<?> type = field.getType();
        
        // Make editable, will set back to the original at the end of this call.
        boolean wasAccessible = field.isAccessible();
        field.setAccessible(true);
        
        NBTClassType<?> action = NBTClassType.NBTYPES.get(type);
        if(action != null) {
            try {
                action.persistedFieldAction(tile, field, tag, write);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Could not access field " + field.getName() + " in " + tile.getClass());
            }
        } else {
            throw new RuntimeException("No NBT persist action found for field " + field.getName() + " of class " + type + " in " + tile.getClass());
        }
        field.setAccessible(wasAccessible);
    }
    
    /**
     * Called to read or write a field.
     * @param tile The tile entity that has the field.
     * @param field The field to persist or read.
     * @param tag The tag compound to read or write to.
     * @param write If there should be written, otherwise there will be read.
     * @throws IllegalArgumentException Argument exception;
     * @throws IllegalAccessException Access exception;
     */
    @SuppressWarnings("unchecked")
    public void persistedFieldAction(EvilCraftTileEntity tile, Field field, NBTTagCompound tag, boolean write) throws IllegalAccessException {
        String name = field.getName();
        if(write) {
            try {
                T object = (T) field.get(tile);
                writePersistedField(name, object, tag);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Can not write the field " + field.getName() + " in " + tile + " since it does not exist.");
            }
        } else {
            T object = null;
            try {
                object = readPersistedField(name, tag);
                field.set(tile, object);
            }  catch (IllegalArgumentException e) {
                throw new RuntimeException("Can not read the field " + field.getName() + " as " + object + " in " + tile + " since it does not exist OR there is a class mismatch.");
            }
        }
    }
    
    protected abstract void writePersistedField(String name, T object, NBTTagCompound tag);
    protected abstract T readPersistedField(String name, NBTTagCompound tag);
    
}
