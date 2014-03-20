package evilcraft.api.config.configurable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import evilcraft.api.config.ElementType;
import evilcraft.api.config.ExtendedConfig;
import evilcraft.api.config.VillagerConfig;

/**
 * Villager that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableVillager implements Configurable, IVillageTradeHandler {

    protected ExtendedConfig<VillagerConfig> eConfig = null;
    
    /**
     * The type of this {@link Configurable}.
     */
    public static ElementType TYPE = ElementType.VILLAGER;
    
    // The weights of the output - inputs must be less than than maxWeightDifference
    protected List<WeightedItemStack> allowedTradeInputs = new ArrayList<WeightedItemStack>();
    protected List<WeightedItemStack> allowedTradeOutputs = new ArrayList<WeightedItemStack>();
    protected int maxWeightDifference = 5;
    protected int inputMinStackSize = 1;
    protected int inputMaxStackSize = 64;
    protected int addedRecipes = 0;
    protected int requiredAddedRecipes = 10;
    private int attemptAddRecipeUpperbound = 100;
    private int attemptAddRecipe = 0;
    
    /**
     * Make a new instance of a villager.
     * @param eConfig The config for this villager.
     */
    @SuppressWarnings("rawtypes")
    protected ConfigurableVillager(ExtendedConfig eConfig) {
        this.setConfig(eConfig);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setConfig(ExtendedConfig eConfig) {
        this.eConfig = eConfig;
    }
    
    @Override
    public String getUniqueName() {
        return "villager."+eConfig.NAMEDID;
    }
    
    @Override
    public boolean isEntity() {
        return true;
    }

    @Override
    public void manipulateTradesForVillager(EntityVillager villager,
            MerchantRecipeList recipeList, Random random) {
        if (villager.getProfession() == eConfig.downCast().ID) {
            while(addedRecipes < requiredAddedRecipes && attemptAddRecipe < attemptAddRecipeUpperbound) {                
                WeightedItemStack outputWeighted = getRandomOutput(random);
                WeightedItemStack inputAWeighted = getRandomInput(random);
                if(!tryAddingRecipe(recipeList, random, inputAWeighted, null, outputWeighted)) {
                    WeightedItemStack inputBWeighted = getRandomInput(random);
                    tryAddingRecipe(recipeList, random, inputAWeighted, inputBWeighted, outputWeighted);
                }
            }
        }
    }
    
    /**
     * This will try adding the recipe to the recipelist if the weights of the inputs are
     * almost the same as the output.
     * In that case it will calculate random stack sizes for the input(s) and derive an appropriate
     * output stack size depending on the normalized weights.
     * @param recipeList The MerchantRecipeList.
     * @param random Random instance.
     * @param inputAWeighted Weighted first input.
     * @param inputBWeighted Weighted second input, can be null.
     * @param outputWeighted Weighted output.
     * @return If the recipe could be added.
     */
    private boolean tryAddingRecipe(MerchantRecipeList recipeList, Random random, WeightedItemStack inputAWeighted, WeightedItemStack inputBWeighted, WeightedItemStack outputWeighted) {
        attemptAddRecipe++;
        int inputWeight = inputAWeighted.getWeight();
        if(inputBWeighted != null)
            inputWeight += inputBWeighted.getWeight();
        if(outputWeighted.getWeight() - inputWeight >= maxWeightDifference)
            return false;
        
        ItemStack inputA;
        ItemStack inputB;
        ItemStack output;
        
        int totalInputWeight = 0;
        
        inputA = inputAWeighted.getItemStack().copy();
        inputA.stackSize = Math.min(inputA.stackSize, getRandomBetween(random, inputMinStackSize, inputMaxStackSize));
        totalInputWeight += inputA.stackSize * inputAWeighted.getItemStack().stackSize / inputAWeighted.getWeight();
        
        if(inputBWeighted != null) {
            inputB = inputAWeighted.getItemStack().copy();
            inputB.stackSize = Math.min(inputB.stackSize, getRandomBetween(random, inputMinStackSize, inputMaxStackSize));
            totalInputWeight += inputB.stackSize * inputBWeighted.getItemStack().stackSize / inputBWeighted.getWeight();
        } else {
            inputB = null;
        }
        
        output = outputWeighted.getItemStack().copy();
        output.stackSize = Math.min(64, Math.max(1, safeDivide(totalInputWeight, (safeDivide(outputWeighted.getItemStack().stackSize, outputWeighted.getWeight())))));
        
        recipeList.addToListWithCheck(new MerchantRecipe(inputA, inputB, output));
        addedRecipes++;
        return true;
    }
    
    protected int safeDivide(int i, int j) {
        return i / Math.max(1, j);
    }
    
    protected WeightedItemStack getRandomWeightedItemStack(List<WeightedItemStack> trades, Random random) {
        return trades.get(random.nextInt(trades.size()));
    }
    
    protected WeightedItemStack getRandomInput(Random random) {
        return getRandomWeightedItemStack(allowedTradeInputs, random);
    }
    
    protected WeightedItemStack getRandomOutput(Random random) {
        return getRandomWeightedItemStack(allowedTradeOutputs, random);
    }
    
    protected int getRandomBetween(Random random, int min, int max) {
        return min + random.nextInt(max - min);
    }
    
    /**
     * An ItemStack that has a certain weight that can be used to occur with a certain chance.
     * @author rubensworks
     *
     */
    public class WeightedItemStack{
        private ItemStack itemStack;
        private int weight;
        
        /**
         * Make a new instance.
         * @param itemStack The ItemStack to weigh.
         * @param weight The weight of the ItemStack.
         */
        public WeightedItemStack(ItemStack itemStack, int weight) {
            this.itemStack = itemStack;
            this.weight = weight;
        }
        
        /**
         * Get the ItemStack.
         * @return The ItemStack.
         */
        public ItemStack getItemStack() {
            return itemStack;
        }
        
        /**
         * Get the weight.
         * @return The weight.
         */
        public int getWeight() {
            return weight;
        }
    }

}
