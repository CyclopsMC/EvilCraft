package org.cyclops.evilcraft.core.recipe.custom;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.component.IItemStackRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.ItemStackRecipeComponent;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.core.weather.WeatherType;

import java.util.List;

/**
 * A recipe component that can be used in {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe}S for the
 * {@link EnvironmentalAccumulator}.
 * @author immortaleeb
 */
public class EnvironmentalAccumulatorRecipeComponent implements IRecipeInput, IRecipeOutput, IItemStackRecipeComponent, IWeatherTypeRecipeComponent {
    private final ItemStackRecipeComponent itemStack;
    private final WeatherTypeRecipeComponent weatherType;

    public EnvironmentalAccumulatorRecipeComponent(ItemStack itemStack, WeatherType weatherType) {
        this.itemStack = new ItemStackRecipeComponent(itemStack);
        this.weatherType = new WeatherTypeRecipeComponent(weatherType);
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack.getItemStack();
    }

    /**
     * Get the itemstack and merge its nbt data from the given itemstack.
     * @param inputStack The stack to copy nbt data from.
     * @return The new itemstack.
     */
    public ItemStack getConditionalItemStack(ItemStack inputStack) {
        ItemStack itemStack = getItemStack().copy();
        if(inputStack.hasTagCompound()) {
            if(!itemStack.hasTagCompound()) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            for (String key : inputStack.getTagCompound().getKeySet()) {
                if(!itemStack.getTagCompound().hasKey(key)) {
                    itemStack.getTagCompound().setTag(key, inputStack.getTagCompound().getTag(key));
                }
            }
        }
        return itemStack;
    }

    public List<ItemStack> getItemStacks() {
        return itemStack.getItemStacks();
    }

    @Override
    public WeatherType getWeatherType() {
        return weatherType.getWeatherType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnvironmentalAccumulatorRecipeComponent)) return false;

        EnvironmentalAccumulatorRecipeComponent that = (EnvironmentalAccumulatorRecipeComponent) o;

        if (!itemStack.equals(that.itemStack)) return false;
        if (!weatherType.equals(that.weatherType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemStack.hashCode();
        result = 31 * result + weatherType.hashCode();
        return result;
    }
}
