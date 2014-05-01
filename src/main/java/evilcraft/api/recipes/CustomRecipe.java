package evilcraft.api.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * A custom recipe that has an input itemStack, fluidStack and a factory in
 * which an output can be created. The output has to be stored in
 * {@link CustomRecipeResult} and both have to be stored in a map in
 * {@link CustomRecipeRegistry}.
 * A minimal recipe is the minimum part of this recipe that is required to
 * be able to get an output. This minimal recipe will be compared with
 * an other {@link CustomRecipe} and the equals method will return true if the
 * following conditions are met:
 * <ul>
 *  <li>The item id's of both itemStacks are equal</li>
 *  <li>The factory blocks are equal.</li>
 *  <li>The fluid is equal (or both null).</li>
 *  <li>The fluid amount of the comparing recipe is equal to or greater
 *  than the minimal recipe. (or both zero)</li>
 * </ul>
 * @see CustomRecipeRegistry
 * @see CustomRecipeResult
 * @author rubensworks
 *
 */
public class CustomRecipe {
    
    private ItemStack itemStack;
    private FluidStack fluidStack;
    private Block factory;
    private int duration = 20;
    private String namedId;
    
    /**
     * Create a new Custom Recipe without a duration.
     * @param itemStack The input itemStack
     * @param fluidStack The input fluidStack (this specifies the minimum amount
     * of a given fluid has to be available.
     * @param factory The factory block in which this recipe applies.
     */
    public CustomRecipe(ItemStack itemStack, FluidStack fluidStack, Block factory) {
        this(null, itemStack, fluidStack, factory, 0);
    }
    
    /**
     * Create a new Custom Recipe without a duration.
     * @param namedId The (optional) named id for this recipe.
     * @param itemStack The input itemStack
     * @param fluidStack The input fluidStack (this specifies the minimum amount
     * of a given fluid has to be available.
     * @param factory The factory block in which this recipe applies.
     */
    public CustomRecipe(String namedId, ItemStack itemStack, FluidStack fluidStack, Block factory) {
        this(namedId, itemStack, fluidStack, factory, 0);
    }
    
    /**
     * Create a new Custom Recipe with a duraction.
     * @param itemStack The input itemStack
     * @param fluidStack The input fluidStack (this specifies the minimum amount
     * of a given fluid has to be available.
     * @param factory The factory block in which this recipe applies.
     * @param duration The amount of ticks the crafting should take.
     */
    public CustomRecipe(ItemStack itemStack, FluidStack fluidStack, Block factory, int duration) {
        this(null, itemStack, fluidStack, factory, duration);
    }
    
    /**
     * Create a new Custom Recipe with a duraction.
     * @param namedId The (optional) named id for this recipe.
     * @param itemStack The input itemStack
     * @param fluidStack The input fluidStack (this specifies the minimum amount
     * of a given fluid has to be available.
     * @param factory The factory block in which this recipe applies.
     * @param duration The amount of ticks the crafting should take.
     */
    public CustomRecipe(String namedId, ItemStack itemStack, FluidStack fluidStack, Block factory, int duration) {
        this.namedId = namedId;
        this.itemStack = itemStack;
        this.fluidStack = fluidStack;
        this.factory = factory;
        this.duration = duration;
    }
    
    /**
     * Get the itemStack that is required as input.
     * @return the input itemStack.
     */
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    /**
     * Get the minimum required amount of the given fluid for this recipe.
     * @return The fluidStack of this recipe.
     */
    public FluidStack getFluidStack() {
        return fluidStack;
    }

    /**
     * Get the factory block in which this recipe can be made.
     * @return The factory block.
     */
    public Block getFactory() {
        return factory;
    }
    
    /**
     * Get the duration in ticks for this recipe.
     * @return Duration in ticks.
     */
    public int getDuration() {
        return this.duration;
    }
    
    /**
     * Returns the named id of this recipe.
     * Note that this can be null, since the
     * named id is optional.
     * @return The named id of this recipe, or
     *         null in case no id was assigned to
     *         this recipe.
     */
    public String getNamedId() {
        return namedId;
    }
    
    @Override
    public boolean equals(Object object) {
        if(!(object instanceof CustomRecipe)) return false;
        CustomRecipe o = (CustomRecipe) object;
        boolean item = this.getItemStack().getItem().equals(o.getItemStack().getItem());
        boolean fluid = true;
        boolean factory = this.getFactory() == o.getFactory();
        if(this.getFluidStack() != null) {
            fluid = false;
            if(o.getFluidStack() != null) {
                if(o.getFluidStack() != null && this.getFluidStack().getFluid().equals(o.getFluidStack().getFluid())) {
                    fluid = o.getFluidStack().amount >= this.getFluidStack().amount;
                }
            }
        }
        
        return item && fluid && factory;
    }
}
