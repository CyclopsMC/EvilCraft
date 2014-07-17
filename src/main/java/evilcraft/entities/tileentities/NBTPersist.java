package evilcraft.entities.tileentities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import evilcraft.api.INBTSerializable;
import evilcraft.api.entities.tileentitites.EvilCraftTileEntity;

/**
 * If this field should be persisted in Tile Entities.
 * Fields that are a subtype of {@link INBTSerializable} must not be null, they should
 * have a dummy value that will then be populated with the actual values.
 * It will automatically be added to
 * {@link EvilCraftTileEntity#writeToNBT(net.minecraft.nbt.NBTTagCompound)}
 * and {@link EvilCraftTileEntity#readFromNBT(net.minecraft.nbt.NBTTagCompound)}.
 * @author rubensworks
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)
public @interface NBTPersist {
	
}
