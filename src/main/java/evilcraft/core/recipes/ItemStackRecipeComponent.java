package evilcraft.core.recipes;

import lombok.Data;
import net.minecraft.item.ItemStack;

/**
 * A {@link evilcraft.core.recipes.IRecipe} component (input, output or properties) that holds an
 * {@link net.minecraft.item.ItemStack}.
 *
 * @author immortaleeb
 */
@Data
public class ItemStackRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IItemStackRecipeComponent {
    private final ItemStack itemStack;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ItemStackRecipeComponent)) return false;
        ItemStackRecipeComponent that = (ItemStackRecipeComponent)object;

        if (this.itemStack != null) {
            return that.itemStack != null
                    && this.itemStack.getItem().equals(that.itemStack.getItem())
                    && this.itemStack.getItemDamage() == that.itemStack.getItemDamage();
        }

        return that.itemStack == null;
    }

    @Override
    public int hashCode() {
        return itemStack.getItem().hashCode() + 876;
    }
}
