package evilcraft.core.recipe.custom;

import evilcraft.api.recipes.custom.IRecipeInput;
import evilcraft.api.recipes.custom.IRecipeOutput;
import evilcraft.core.weather.WeatherType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Set;

/**
 * A recipe component that can be used in {@link evilcraft.api.recipes.custom.IRecipe}S for the
 * {@link evilcraft.block.EnvironmentalAccumulator}.
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
        if(inputStack != null && inputStack.hasTagCompound()) {
            if(!itemStack.hasTagCompound()) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            for (String key : (Set<String>) inputStack.getTagCompound().func_150296_c()) {
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
