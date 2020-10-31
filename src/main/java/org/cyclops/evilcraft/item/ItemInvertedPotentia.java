package org.cyclops.evilcraft.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.evilcraft.RegistryEntries;
import org.cyclops.evilcraft.entity.item.EntityItemEmpowerable;

import java.util.List;

/**
 * An inverted Potentia Sphere.
 * @author rubensworks
 *
 */
public class ItemInvertedPotentia extends Item implements IItemEmpowerable {

    private final boolean empowered;

    public ItemInvertedPotentia(Properties properties, boolean empowered) {
        super(properties);
        this.empowered = empowered;
    }
    
    @Override
    public boolean hasEffect(ItemStack itemStack){
        return isEmpowered(itemStack);
    }
    
    @Override
	public ItemStack empower(ItemStack itemStack) {
        return new ItemStack(RegistryEntries.ITEM_INVERTED_POTENTIA_EMPOWERED);
    }
    
    @Override
	public boolean isEmpowered(ItemStack itemStack) {
        return this.empowered;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        if(isEmpowered(itemStack))
            list.add(new StringTextComponent("Empowered").applyTextStyle(TextFormatting.RED));
    }
    
    @Override
    public boolean hasCustomEntity(ItemStack itemStack) {
    	return true;
    }
    
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemStack) {
    	return new EntityItemEmpowerable(world, (ItemEntity) location);
    }

}
