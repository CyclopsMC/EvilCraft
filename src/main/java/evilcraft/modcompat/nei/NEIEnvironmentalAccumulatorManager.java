package evilcraft.modcompat.nei;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import evilcraft.Reference;
import evilcraft.api.recipes.CustomRecipe;
import evilcraft.api.recipes.CustomRecipeRegistry;
import evilcraft.api.recipes.CustomRecipeResult;
import evilcraft.api.recipes.EnvironmentalAccumulatorRecipe;
import evilcraft.api.recipes.EnvironmentalAccumulatorResult;
import evilcraft.api.weather.WeatherType;
import evilcraft.blocks.EnvironmentalAccumulator;
import evilcraft.blocks.EnvironmentalAccumulatorConfig;

/**
 * Manager for the recipes in {@link EnvironmentalAccumulator}.
 * @author immortaleeb, rubensworks
 *
 */
public class NEIEnvironmentalAccumulatorManager extends TemplateRecipeHandler {
    
    private static final String WEATHER_ICONS = Reference.MOD_ID + ":" + Reference.TEXTURE_PATH_GUI + "weathers.png";
    private static final Map<WeatherType, Integer> X_ICON_OFFSETS = new HashMap<WeatherType, Integer>();
    static {
        X_ICON_OFFSETS.put(WeatherType.CLEAR, 0);
        X_ICON_OFFSETS.put(WeatherType.RAIN, 16);
        X_ICON_OFFSETS.put(WeatherType.LIGHTNING, 32);
    }
    
    private final int progressTargetX = 77;
    private final int progressTargetY = 0;
    private final int progressX = 166;
    private final int progressY = 0;
    private final int progressWidth = 11;
    private final int progressHeight = 60;
    
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
                        36, 48
                    );
            this.outputStack =
                    new PositionedStack(
                        outputStack,
                        114, 48
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
    public void loadTransferRects() {
        transferRects.add(
                new RecipeTransferRect(
                    new Rectangle(
                            progressTargetX,
                            progressTargetY,
                            progressWidth, progressHeight
                            ),
                    getOverlayIdentifier()
                )
        );
    }
    
    @Override
    public String getOverlayIdentifier() {
        return EnvironmentalAccumulatorConfig._instance.NAMEDID;
    }

    @Override
    public String getRecipeName() {
        return EnvironmentalAccumulator.getInstance().getLocalizedName();
    }

    @Override
    public String getGuiTexture() {
        return Reference.MOD_ID + ":" + EnvironmentalAccumulator.getInstance().getGuiTexture("_nei");
    }
    
    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        changeTexture(getGuiTexture());
        drawTexturedModalRect(0, 0, 0, 0, 166, 165);
    }
    
    private List<CachedEnvironmentalAccumulatorRecipe> getRecipes() {
        List<CachedEnvironmentalAccumulatorRecipe> recipes = new LinkedList<CachedEnvironmentalAccumulatorRecipe>();
        for(CustomRecipeResult recipeResult : CustomRecipeRegistry.getRecipesForFactory(EnvironmentalAccumulator.getInstance()).values()) {
            EnvironmentalAccumulatorRecipe eaRecipe = (EnvironmentalAccumulatorRecipe) recipeResult.getRecipe();
            EnvironmentalAccumulatorResult eaResult = (EnvironmentalAccumulatorResult) recipeResult;
            recipes.add(
                    new CachedEnvironmentalAccumulatorRecipe(
                            recipeResult.getRecipe().getItemStack(),
                            eaRecipe.getWeatherType(),
                            recipeResult.getResult(),
                            eaResult.getWeatherResult(),
                            recipeResult.getRecipe().getDuration()
                            )
                    );
        }
        return recipes;
    }
    
    @Override
    public int recipiesPerPage() {
        return 1;
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals(getOverlayIdentifier())) {
            for(CachedEnvironmentalAccumulatorRecipe recipe : getRecipes()) {
                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
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
                new EnvironmentalAccumulatorRecipe(
                        null,
                        ingredient
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
    
    /*@Override
    public void drawExtras(int recipeIndex) {
        CachedEnvironmentalAccumulatorRecipe recipe = (CachedEnvironmentalAccumulatorRecipe)arecipes.get(recipeIndex);
        
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glRectf(0.0f, 0.0f, 10f, 10f);
    }*/
    
    private CachedEnvironmentalAccumulatorRecipe getRecipe(int recipe) {
        return (CachedEnvironmentalAccumulatorRecipe) arecipes.get(recipe);
    }
    
    @Override
    public void drawExtras(int recipe) {
        CachedEnvironmentalAccumulatorRecipe cachedRecipe = getRecipe(recipe);
        drawProgressBar(
                progressTargetX,
                progressTargetY,
                progressX,
                progressY,
                progressWidth,
                progressHeight,
                Math.max(2, cachedRecipe.duration / 10),
                3);
        
        int inputX = X_ICON_OFFSETS.get(cachedRecipe.inputWeather);
        changeTexture(WEATHER_ICONS);
        drawTexturedModalRect(36, 28, inputX, 0, 16, 16);
        
        int outputX = X_ICON_OFFSETS.get(cachedRecipe.outputWeather);
        changeTexture(WEATHER_ICONS);
        drawTexturedModalRect(114, 28, outputX, 0, 16, 16);
    }
}
