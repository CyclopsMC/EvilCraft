package evilcraft.core.recipe.custom;

import lombok.Data;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * A {@link evilcraft.api.recipes.custom.IRecipe} component (input, output or properties) that holds an
 * oredictionary key.
 *
 * @author immortaleeb
 */
@Data
public class OreDictItemStackRecipeComponent extends ItemStackRecipeComponent {

    private final String key;
    @Getter(lazy=true) private final List<ItemStack> itemStacks = OreDictionary.getOres(getKey());

    public OreDictItemStackRecipeComponent(String key) {
        super(null);
        this.key = key;
    }

    @Override
    public boolean equals(Object object) {
        if(super.equals(object)) {
            return true;
        }

        if (!(object instanceof ItemStackRecipeComponent)) return false;
        ItemStackRecipeComponent that = (ItemStackRecipeComponent)object;

        // To increase performance, first check if the comparing stack is not null before
        // potentially matching it with the whole oredict.
        if(that.getItemStack() != null) {
            for (ItemStack itemStack : getItemStacks()) {
                if (equals(itemStack, that.getItemStack())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return key.hashCode() + 876;
    }

}
