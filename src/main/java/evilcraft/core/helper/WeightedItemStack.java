package evilcraft.core.helper;

import com.google.common.collect.Lists;
import lombok.Data;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * An ItemStack that has a certain weight that can be used to occur with a certain chance.
 *
 * @author rubensworks
 */
@Data
public class WeightedItemStack {

    @Nullable
    private final ItemStack itemStack;
    private final int weight;

    /**
     * Create a weighted list of the given input items.
     * @param input The set of unique weighted itemstacks.
     * @return A list which can be used to get random weighted itemstacks.
     */
    public static List<WeightedItemStack> createWeightedList(Set<WeightedItemStack> input) {
        List<WeightedItemStack> trueList = Lists.newLinkedList();
        for(WeightedItemStack itemStack : input) {
            for(int i = 0; i < itemStack.getWeight(); i++) {
                trueList.add(itemStack);
            }
        }
        return trueList;
    }

    /**
     * Get a random item from the given list.
     * @param list The list, generated from {@link evilcraft.core.helper.WeightedItemStack#createWeightedList}
     * @param random A random instance.
     * @return A random item.
     */
    public static WeightedItemStack getRandomWeightedItemStack(List<WeightedItemStack> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Get a copy of the itemstack with a randomized stacksize.
     * The original itemstack size represent the maximum stacksize +1.
     * @param random A random instance.
     * @return A new itemstack.
     */
    public ItemStack getItemStackWithRandomizedSize(Random random) {
        if(getItemStack() == null) {
            return null;
        }
        ItemStack itemStack = getItemStack().copy();
        itemStack.stackSize = random.nextInt(itemStack.stackSize) + 1;
        return itemStack;
    }

    @Override
    public String toString() {
        return "{ItemStack: " + itemStack + "; Weight: " + weight + "}";
    }

}
