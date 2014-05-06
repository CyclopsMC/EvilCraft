package evilcraft.modcompat.nei;

import static codechicken.core.gui.GuiDraw.changeTexture;
import static codechicken.core.gui.GuiDraw.drawTexturedModalRect;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import evilcraft.Reference;
import evilcraft.api.recipes.CustomRecipe;
import evilcraft.api.recipes.CustomRecipeRegistry;
import evilcraft.api.recipes.EnvironmentalAccumulatorRecipe;
import evilcraft.api.recipes.EnvironmentalAccumulatorResult;
import evilcraft.api.weather.WeatherType;
import evilcraft.blocks.EnvironmentalAccumulator;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;

/**
 * Manager for the recipes in {@link EnvironmentalAccumulator}.
 * @author immortaleeb
 *
 */
public class NEIEnvironmentalAccumulatorManager extends TemplateRecipeHandler {
    
    private class CachedEnvironmentalAccumulatorRecipe extends CachedRecipe {
        
        private PositionedStack inputStack;
        private WeatherType inputWeather;
        private PositionedStack outputStack;
        private WeatherType outputWeather;
        private int duration;
        
        public CachedEnvironmentalAccumulatorRecipe(
                ItemStack inputStack, WeatherType inputWeather,
                ItemStack outputStack, WeatherType outputWeather,
                int duration) {
            this.inputStack = 
                    new PositionedStack(
                        inputStack,
                        0, 0
                    );
            this.outputStack =
                    new PositionedStack(
                        outputStack,
                        100, 10
                    );
            this.inputWeather = inputWeather;
            this.outputWeather = outputWeather;
            this.duration = duration;
        }
        
        @Override
        public PositionedStack getIngredient() {
            return inputStack;
        }

        @Override
        public PositionedStack getResult() {
            return outputStack;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + duration;
            result = prime * result
                    + ((inputStack == null) ? 0 : inputStack.hashCode());
            result = prime * result
                    + ((inputWeather == null) ? 0 : inputWeather.hashCode());
            result = prime * result
                    + ((outputStack == null) ? 0 : outputStack.hashCode());
            result = prime * result
                    + ((outputWeather == null) ? 0 : outputWeather.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CachedEnvironmentalAccumulatorRecipe other = (CachedEnvironmentalAccumulatorRecipe) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (duration != other.duration)
                return false;
            if (inputStack == null) {
                if (other.inputStack != null)
                    return false;
            } else if (!inputStack.equals(other.inputStack))
                return false;
            if (inputWeather == null) {
                if (other.inputWeather != null)
                    return false;
            } else if (!inputWeather.equals(other.inputWeather))
                return false;
            if (outputStack == null) {
                if (other.outputStack != null)
                    return false;
            } else if (!outputStack.equals(other.outputStack))
                return false;
            if (outputWeather == null) {
                if (other.outputWeather != null)
                    return false;
            } else if (!outputWeather.equals(other.outputWeather))
                return false;
            return true;
        }

        private NEIEnvironmentalAccumulatorManager getOuterType() {
            return NEIEnvironmentalAccumulatorManager.this;
        }
        
    }

    @Override
    public String getRecipeName() {
        return EnvironmentalAccumulatorConfig._instance.NAME;
    }

    @Override
    public String getGuiTexture() {
        return Reference.MOD_ID + ":" + Reference.TEXTURE_PATH_GUI + "ea_clear_nei.png";
    }
    
    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        changeTexture(getGuiTexture());
        drawTexturedModalRect(-5, -16, 0, 0, 176, 166);
    }
    
    @Override
    public int recipiesPerPage() {
        return 1;
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        CustomRecipe recipe = CustomRecipeRegistry.get(result);
        if(recipe != null && recipe.getFactory() == EnvironmentalAccumulator.getInstance()) {
            EnvironmentalAccumulatorRecipe eaRecipe = (EnvironmentalAccumulatorRecipe)recipe;
            EnvironmentalAccumulatorResult eaResult = (EnvironmentalAccumulatorResult) CustomRecipeRegistry.get(eaRecipe);
            
            arecipes.add(
                    new CachedEnvironmentalAccumulatorRecipe(
                            eaRecipe.getItemStack(),
                            eaRecipe.getWeatherType(),
                            eaResult.getItemResult(),
                            eaResult.getWeatherResult(),
                            eaRecipe.getDuration()
                            )
                    );
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        CustomRecipe customRecipeKey =
                new CustomRecipe(
                    ingredient,
                    null,
                    EnvironmentalAccumulator.getInstance()
                );
        EnvironmentalAccumulatorResult eaResult = (EnvironmentalAccumulatorResult) CustomRecipeRegistry.get(customRecipeKey);
        if(eaResult != null) {
            EnvironmentalAccumulatorRecipe eaRecipe = (EnvironmentalAccumulatorRecipe) CustomRecipeRegistry.get(eaResult.getItemResult());
            
            arecipes.add(
                    new CachedEnvironmentalAccumulatorRecipe(
                            eaRecipe.getItemStack(),
                            eaRecipe.getWeatherType(),
                            eaResult.getItemResult(),
                            eaResult.getWeatherResult(),
                            eaRecipe.getDuration()
                            )
                    );
        }
    }
    
    @Override
    public void drawExtras(int recipeIndex) {
        CachedEnvironmentalAccumulatorRecipe recipe = (CachedEnvironmentalAccumulatorRecipe)arecipes.get(recipeIndex);
        
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glRectf(0.0f, 0.0f, 10f, 10f);
    }
}
