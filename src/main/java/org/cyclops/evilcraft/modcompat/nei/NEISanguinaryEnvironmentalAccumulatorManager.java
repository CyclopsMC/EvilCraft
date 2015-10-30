package org.cyclops.evilcraft.modcompat.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.evilcraft.Reference;
import org.cyclops.evilcraft.block.EnvironmentalAccumulator;
import org.cyclops.evilcraft.block.SanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.block.SanguinaryEnvironmentalAccumulatorConfig;
import org.cyclops.evilcraft.client.gui.container.GuiBloodInfuser;
import org.cyclops.evilcraft.client.gui.container.GuiSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.core.helper.RenderHelpers;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeComponent;
import org.cyclops.evilcraft.core.recipe.custom.EnvironmentalAccumulatorRecipeProperties;
import org.cyclops.evilcraft.core.weather.WeatherType;
import org.cyclops.evilcraft.inventory.container.ContainerSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.TileBloodInfuser;
import org.cyclops.evilcraft.tileentity.TileSanguinaryEnvironmentalAccumulator;
import org.cyclops.evilcraft.tileentity.tickaction.sanguinaryenvironmentalaccumulator.AccumulateItemTickAction;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

/**
 * Manager for the recipes in {@link SanguinaryEnvironmentalAccumulator}.
 * TODO: this could be abstracted when compared to {@link NEIBloodInfuserManager}.
 * @author rubensworks
 *
 */
public class NEISanguinaryEnvironmentalAccumulatorManager extends TemplateRecipeHandler {

    private static final String WEATHER_ICONS = Reference.MOD_ID + ":" + Reference.TEXTURE_PATH_GUI + "weathers.png";
    private static final Map<WeatherType, Integer> X_ICON_OFFSETS = new HashMap<WeatherType, Integer>();
    static {
        X_ICON_OFFSETS.put(WeatherType.CLEAR, 0);
        X_ICON_OFFSETS.put(WeatherType.RAIN, 16);
        X_ICON_OFFSETS.put(WeatherType.LIGHTNING, 32);
    }

    protected static int xOffset = -5;
    protected static int yOffset = -16;

    private final int width = GuiBloodInfuser.TEXTUREWIDTH;
    private final int height = GuiBloodInfuser.TEXTUREHEIGHT;
    private final int tankWidth = GuiBloodInfuser.TANKWIDTH;
    private final int tankHeight = GuiBloodInfuser.TANKHEIGHT;
    private final int tankTargetX = 8 + xOffset;
    private final int tankTargetY = 72 + yOffset;
    private final int tankX = GuiBloodInfuser.TANKX;
    private final int tankY = GuiBloodInfuser.TANKY;
    
    private final int progressTargetX = GuiSanguinaryEnvironmentalAccumulator.PROGRESSTARGETX + xOffset;
    private final int progressTargetY = GuiSanguinaryEnvironmentalAccumulator.PROGRESSTARGETY + yOffset;
    private final int progressX = GuiSanguinaryEnvironmentalAccumulator.PROGRESSX;
    private final int progressY = GuiSanguinaryEnvironmentalAccumulator.PROGRESSY;
    private final int progressWidth = GuiSanguinaryEnvironmentalAccumulator.PROGRESSWIDTH;
    private final int progressHeight = GuiSanguinaryEnvironmentalAccumulator.PROGRESSHEIGHT;

    private float zLevel = 200.0F;
    
    private class CachedEnvironmentalAccumulatorRecipe extends CachedRecipe {
        
        private PositionedStack inputStack;
        private WeatherType inputWeather;
        private PositionedStack outputStack;
        private int duration;
        private FluidStack fluidStack;
        private Rectangle tank;

        public CachedEnvironmentalAccumulatorRecipe(
                EnvironmentalAccumulatorRecipeComponent input,
                EnvironmentalAccumulatorRecipeComponent output,
                EnvironmentalAccumulatorRecipeProperties properties) {
            this(input.getItemStack(), input.getWeatherType(),
                    output.getItemStack(),
                    properties.getDuration(), new FluidStack(TileSanguinaryEnvironmentalAccumulator.ACCEPTED_FLUID,
                            AccumulateItemTickAction.getUsage(properties)));
        }
        
        public CachedEnvironmentalAccumulatorRecipe(
                ItemStack inputStack, WeatherType inputWeather,
                ItemStack outputStack,
                int duration, FluidStack fluidStack) {
            this.inputStack = 
                    new PositionedStack(
                        inputStack,
                            ContainerSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE_X + xOffset,
                            ContainerSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE_Y + yOffset
                    );
            this.outputStack =
                    new PositionedStack(
                            ItemStackHelpers.getVariants(outputStack),
                            ContainerSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE_RESULT_X + xOffset,
                            ContainerSanguinaryEnvironmentalAccumulator.SLOT_ACCUMULATE_RESULT_Y + yOffset
                    );
            //this.outputStack.setPermutationToRender(1);
            this.inputWeather = inputWeather;
            this.duration = duration;
            this.fluidStack = fluidStack;
            if(this.fluidStack != null) {
                tank = new Rectangle(tankTargetX, -1, tankWidth, tankHeight);
            }
        }
        
        @Override
        public PositionedStack getIngredient() {
            return inputStack;
        }

        @Override
        public PositionedStack getResult() {
            outputStack.setPermutationToRender((cycleticks / 32) % outputStack.items.length);
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
                    + ((fluidStack == null) ? 0 : fluidStack.hashCode());
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
            if (fluidStack == null) {
                if (other.fluidStack != null)
                    return false;
            } else if (!fluidStack.equals(other.fluidStack))
                return false;
            return true;
        }

        private NEISanguinaryEnvironmentalAccumulatorManager getOuterType() {
            return NEISanguinaryEnvironmentalAccumulatorManager.this;
        }
    }

    public NEISanguinaryEnvironmentalAccumulatorManager() {
        LinkedList<RecipeTransferRect> guiTransferRects = new LinkedList<RecipeTransferRect>();
        guiTransferRects.add(
                new RecipeTransferRect(
                        new Rectangle(
                                GuiSanguinaryEnvironmentalAccumulator.PROGRESSTARGETX + GuiSanguinaryEnvironmentalAccumulator.UPGRADES_OFFSET_X - 6,
                                GuiSanguinaryEnvironmentalAccumulator.PROGRESSTARGETY - 10,
                                progressWidth, progressHeight
                        ),
                        getSEAOverlayIdentifier()
                )
        );

        LinkedList<Class<? extends GuiContainer>> list = new LinkedList<Class<? extends GuiContainer>>();
        list.add(getGuiClass());
        RecipeTransferRectHandler.registerRectsToGuis(list, guiTransferRects);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiSanguinaryEnvironmentalAccumulator.class;
    }

    @Override
    public void loadTransferRects() {
        transferRects.clear();
        transferRects.add(
                new RecipeTransferRect(
                        new Rectangle(
                                GuiSanguinaryEnvironmentalAccumulator.PROGRESSTARGETX - 5,
                                GuiSanguinaryEnvironmentalAccumulator.PROGRESSTARGETY - 15,
                                progressWidth, progressHeight
                        ),
                        getSEAOverlayIdentifier()
                )
        );
        transferRects.add(
                new RecipeTransferRect(
                        new Rectangle(
                                tankTargetX - 5,
                                tankY - tankHeight - 15,
                                tankWidth, tankHeight
                        ),
                        getFluidOverlayIdentifier()
                )
        );
    }

    private String getSEAOverlayIdentifier() {
        return SanguinaryEnvironmentalAccumulatorConfig._instance.getNamedId();
    }

    protected String getFluidOverlayIdentifier() {
        return "liquid";
    }

    @Override
    public List<Class<? extends GuiContainer>> getRecipeTransferRectGuis() {
        return null; // We will do transfer rect registering ourselves.
    }
    
    @Override
    public String getOverlayIdentifier() {
        return getSEAOverlayIdentifier();
    }

    @Override
    public String getRecipeName() {
        return SanguinaryEnvironmentalAccumulator.getInstance().getLocalizedName();
    }

    @Override
    public String getGuiTexture() {
        return Reference.MOD_ID + ":" + SanguinaryEnvironmentalAccumulator.getInstance().getGuiTexture("_nei");
    }
    
    private List<CachedEnvironmentalAccumulatorRecipe> getRecipes() {
        List<CachedEnvironmentalAccumulatorRecipe> recipes = new LinkedList<CachedEnvironmentalAccumulatorRecipe>();
        for (IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent,
                        EnvironmentalAccumulatorRecipeProperties> recipe : EnvironmentalAccumulator.getInstance().getRecipeRegistry()
        		.allRecipes()) {
            EnvironmentalAccumulatorRecipeComponent input = (EnvironmentalAccumulatorRecipeComponent)recipe.getInput();
            EnvironmentalAccumulatorRecipeComponent output = (EnvironmentalAccumulatorRecipeComponent)recipe.getOutput();
            EnvironmentalAccumulatorRecipeProperties props = (EnvironmentalAccumulatorRecipeProperties)recipe.getProperties();

            recipes.add(new CachedEnvironmentalAccumulatorRecipe(input, output, props));
        }
        return recipes;
    }
    
    @Override
    public int recipiesPerPage() {
        return 2;
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
    public void loadCraftingRecipes(final ItemStack result) {
        IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent,
        	EnvironmentalAccumulatorRecipeProperties> recipe = EnvironmentalAccumulator.getInstance().getRecipeRegistry()
        	.findRecipeByOutput(
                new EnvironmentalAccumulatorRecipeComponent(result, WeatherType.ANY)
        );

        if (recipe != null) {
            arecipes.add(new CachedEnvironmentalAccumulatorRecipe(
                    (EnvironmentalAccumulatorRecipeComponent) recipe.getInput(),
                    (EnvironmentalAccumulatorRecipeComponent) recipe.getOutput(),
                    (EnvironmentalAccumulatorRecipeProperties) recipe.getProperties()
            ));
        }
    }
    
    @Override
    public void loadUsageRecipes(final ItemStack ingredient) {
        for (IRecipe<EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeComponent, EnvironmentalAccumulatorRecipeProperties> recipe :
                EnvironmentalAccumulator.getInstance().getRecipeRegistry().findRecipesByInput(new EnvironmentalAccumulatorRecipeComponent(ingredient, WeatherType.ANY))) {
            arecipes.add(new CachedEnvironmentalAccumulatorRecipe(
                    recipe.getInput(),
                    recipe.getOutput(),
                    recipe.getProperties()
            ));
        }
    }
    
    private CachedEnvironmentalAccumulatorRecipe getRecipe(int recipe) {
        return (CachedEnvironmentalAccumulatorRecipe) arecipes.get(recipe);
    }

    @Override
    public void drawExtras(int recipe) {
        CachedEnvironmentalAccumulatorRecipe eaRecipe = getRecipe(recipe);
        drawProgressBar(
                progressTargetX,
                progressTargetY,
                progressX,
                progressY,
                progressWidth,
                progressHeight,
                Math.max(2, eaRecipe.duration / 10),
                0);

        Integer inputX = X_ICON_OFFSETS.get(eaRecipe.inputWeather);
        if(inputX != null) {
            changeTexture(WEATHER_ICONS);
            drawTexturedModalRect(54 + xOffset, 56 + yOffset, inputX, 0, 16, 16);
        }
    }

    protected boolean isFirstOnPage(int recipe) {
        return recipe % recipiesPerPage() == 0;
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        changeTexture(getGuiTexture());
        if(isFirstOnPage(recipe)) {
            drawTexturedModalRect(xOffset, yOffset, 0, 0, width, height);
        } else {
            drawTexturedModalRect(0, -3, -xOffset, -yOffset - 3, 160, 60);
        }
    }

    @Override
    public void drawForeground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        changeTexture(getGuiTexture());
        drawExtras(recipe);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int tankSize = tankHeight;
        drawTank(tankTargetX, tankTargetY, TileBloodInfuser.ACCEPTED_FLUID, tankSize);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected void drawTank(int xOffset, int yOffset, Fluid fluid, int level) {
        Minecraft mc = Minecraft.getMinecraft();
        if(fluid != null) {
            FluidStack stack = new FluidStack(fluid, 1);
            TextureAtlasSprite icon = RenderHelpers.getFluidIcon(stack, EnumFacing.UP);

            int verticalOffset = 0;

            while(level > 0) {
                int textureHeight;

                if(level > 16) {
                    textureHeight = 16;
                    level -= 16;
                } else {
                    textureHeight = level;
                    level = 0;
                }

                mc.renderEngine.bindTexture(RenderHelpers.TEXTURE_MAP);
                drawTexturedModelRectFromIcon(xOffset, yOffset - textureHeight - verticalOffset, icon, tankWidth, textureHeight);
                verticalOffset = verticalOffset + 16;
            }

            changeTexture(getGuiTexture());
            drawTexturedModalRect(xOffset, yOffset - tankHeight, tankX, tankY, tankWidth, tankHeight);
        }
    }

    private void drawTexturedModelRectFromIcon(int x, int y, TextureAtlasSprite icon, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.startDrawingQuads();
        worldRenderer.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMaxV());
        worldRenderer.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMaxV());
        worldRenderer.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMinV());
        worldRenderer.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMinV());
        tessellator.draw();
    }

    @Override
    public List<String> handleTooltip(GuiRecipe guiRecipe, List<String> currenttip, int recipe) {
        super.handleTooltip(guiRecipe, currenttip, recipe);
        CachedEnvironmentalAccumulatorRecipe bloodInfuserRecipe = getRecipe(recipe);
        FluidStack fluid = bloodInfuserRecipe.fluidStack;
        if(fluid != null) {
            Point mouse = GuiDraw.getMousePosition();
            Point offset = guiRecipe.getRecipePosition(recipe);
            Point mouseRelative = new Point(mouse.x - ((guiRecipe.width - width) / 2) - offset.x,
                    mouse.y - ((guiRecipe.height - height) / 2) - offset.y);
            if (bloodInfuserRecipe.tank.contains(mouseRelative)) {
                currenttip.add(fluid.getLocalizedName());
                currenttip.add(fluid.amount + " mB");
            }
        }
        return currenttip;
    }
}
