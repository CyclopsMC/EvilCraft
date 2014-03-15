package evilcraft.nei;

import static codechicken.core.gui.GuiDraw.changeTexture;
import static codechicken.core.gui.GuiDraw.drawTexturedModalRect;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import evilcraft.CustomRecipe;
import evilcraft.CustomRecipeRegistry;
import evilcraft.CustomRecipeResult;
import evilcraft.Reference;
import evilcraft.blocks.BloodInfuser;
import evilcraft.blocks.BloodInfuserConfig;
import evilcraft.entities.tileentities.TileBloodInfuser;
import evilcraft.gui.client.GuiBloodInfuser;
import evilcraft.gui.container.ContainerBloodInfuser;

/**
 * Manager for the recipes in the {@link BloodInfuser}.
 * @author rubensworks
 *
 */
public class NEIBloodInfuserManager extends TemplateRecipeHandler {
    
    private int xOffset = -5;
    private int yOffset = -16;
    
    private final int width = GuiBloodInfuser.TEXTUREWIDTH;
    private final int height = GuiBloodInfuser.TEXTUREHEIGHT;
    private final int tankWidth = GuiBloodInfuser.TANKWIDTH;
    private final int tankHeight = GuiBloodInfuser.TANKHEIGHT;
    private final int tankTargetX = GuiBloodInfuser.TANKTARGETX + xOffset;
    private final int tankTargetY = GuiBloodInfuser.TANKTARGETY + yOffset;
    private final int tankX = GuiBloodInfuser.TANKX;
    private final int tankY = GuiBloodInfuser.TANKY;
    
    private final int progressTargetX = GuiBloodInfuser.PROGRESSTARGETX + xOffset;
    private final int progressTargetY = GuiBloodInfuser.PROGRESSTARGETY + yOffset;
    private final int progressX = GuiBloodInfuser.PROGRESSX;
    private final int progressY = GuiBloodInfuser.PROGRESSY;
    private final int progressWidth = GuiBloodInfuser.PROGRESSWIDTH;
    private final int progressHeight = GuiBloodInfuser.PROGRESSHEIGHT;
    
    private float zLevel = 200.0F;
    
    private class CachedBloodInfuserRecipe extends CachedRecipe {
        
        private int hashcode;
        private PositionedStack input;
        private PositionedStack output;
        private FluidStack fluidStack;
        private int duration;
        
        public CachedBloodInfuserRecipe(ItemStack inputStack, ItemStack outputStack, FluidStack fluidStack, int duration) {
            this.input = 
                    new PositionedStack(
                            inputStack,
                            ContainerBloodInfuser.SLOT_INFUSE_X + xOffset,
                            ContainerBloodInfuser.SLOT_INFUSE_Y + yOffset
                            );
            this.output =
                    new PositionedStack(
                            outputStack,
                            ContainerBloodInfuser.SLOT_INFUSE_RESULT_X + xOffset,
                            ContainerBloodInfuser.SLOT_INFUSE_RESULT_Y + yOffset
                            );
            this.fluidStack = fluidStack;
            this.duration = duration;
            calculateHashcode();
        }
        
        private void calculateHashcode() {
            hashcode = input.item.getItemDamage() << 16 + output.item.getItemDamage();
            hashcode = 31 * hashcode + (input.item.itemID << 16 + output.item.itemID);
        }
        
        @Override
        public boolean equals(Object obj)  {
            if(!(obj instanceof CachedBloodInfuserRecipe))
                return false;
            CachedBloodInfuserRecipe recipe2 = (CachedBloodInfuserRecipe)obj;
            return output.item.getItemDamage() == recipe2.output.item.getItemDamage() && 
                    NEIServerUtils.areStacksSameType(input.item, recipe2.input.item);
        }
        
        @Override
        public int hashCode() {
            return hashcode;
        }

        @Override
        public PositionedStack getResult() {
            return output;
        }
        
        @Override
        public PositionedStack getIngredient() {
            return input;
        }
        
    }
    
    @Override
    public void loadTransferRects() {
        transferRects.add(
                new RecipeTransferRect(
                    new Rectangle(
                            GuiBloodInfuser.PROGRESSTARGETX - 4,
                            GuiBloodInfuser.PROGRESSTARGETY - 8,
                            progressWidth, progressHeight
                            ),
                    getOverlayIdentifier()
                )
        );
    }
    
    @Override
    public String getOverlayIdentifier() {
        return BloodInfuserConfig._instance.NAMEDID;
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiBloodInfuser.class;
    }

    @Override
    public String getRecipeName() {
        return BloodInfuserConfig._instance.NAME;
    }
    
    private List<CachedBloodInfuserRecipe> getRecipes() {
        List<CachedBloodInfuserRecipe> recipes = new LinkedList<CachedBloodInfuserRecipe>();
        for(CustomRecipeResult recipeResult : CustomRecipeRegistry.getRecipesForFactory(BloodInfuser.getInstance()).values()) {
            recipes.add(
                    new CachedBloodInfuserRecipe(
                            recipeResult.getRecipe().getItemStack(),
                            recipeResult.getResult(),
                            recipeResult.getRecipe().getFluidStack(),
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
            for(CachedBloodInfuserRecipe recipe : getRecipes()) {
                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        CustomRecipe recipe = CustomRecipeRegistry.get(result);
        if(recipe != null) {
            arecipes.add(
                    new CachedBloodInfuserRecipe(
                            recipe.getItemStack(),
                            result,
                            recipe.getFluidStack(),
                            recipe.getDuration()
                            )
                    );
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        CustomRecipe customRecipeKey =
                new CustomRecipe(
                    ingredient,
                    new FluidStack(TileBloodInfuser.ACCEPTED_FLUID, TileBloodInfuser.LIQUID_PER_SLOT),
                    BloodInfuser.getInstance()
                );
        CustomRecipeResult recipeResult = CustomRecipeRegistry.get(customRecipeKey);
        if(recipeResult != null) {
            arecipes.add(
                    new CachedBloodInfuserRecipe(
                            recipeResult.getRecipe().getItemStack(),
                            recipeResult.getResult(),
                            recipeResult.getRecipe().getFluidStack(),
                            recipeResult.getRecipe().getDuration()
                            )
                    );
        }
    }

    @Override
    public String getGuiTexture() {
        return Reference.MOD_ID + ":" + BloodInfuser.getInstance().getGuiTexture("_nei");
    }
    
    private CachedBloodInfuserRecipe getRecipe(int recipe) {
        return (CachedBloodInfuserRecipe) arecipes.get(recipe);
    }
    
    @Override
    public void drawExtras(int recipe) {
        CachedBloodInfuserRecipe bloodInfuserRecipe = getRecipe(recipe);
        drawProgressBar(
                progressTargetX,
                progressTargetY,
                progressX,
                progressY,
                progressWidth,
                progressHeight,
                Math.max(2, bloodInfuserRecipe.duration / 10),
                0);
    }
    
    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        changeTexture(getGuiTexture());
        drawTexturedModalRect(xOffset, yOffset, 0, 0, width, height);
    }
    
    @Override
    public void drawForeground(int recipe) {
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        changeTexture(getGuiTexture());
        drawExtras(recipe);
        
        CachedBloodInfuserRecipe bloodInfuserRecipe = getRecipe(recipe);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int tankSize = bloodInfuserRecipe.fluidStack.amount * tankHeight / TileBloodInfuser.LIQUID_PER_SLOT;
        drawTank(tankTargetX, tankTargetY, TileBloodInfuser.ACCEPTED_FLUID.getID(), tankSize);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    protected void drawTank(int xOffset, int yOffset, int fluidID, int level) {
        Minecraft mc = Minecraft.getMinecraft();
        FluidStack stack = new FluidStack(fluidID, 1);
        if(fluidID > 0 && stack != null) {
            Icon icon = stack.getFluid().getIcon();
            if (icon == null) icon = Block.waterMoving.getIcon(0, 0);
            
            int verticalOffset = 0;
            
            while(level > 0) {
                int textureHeight = 0;
                
                if(level > 16) {
                    textureHeight = 16;
                    level -= 16;
                } else {
                    textureHeight = level;
                    level = 0;
                }
                
                mc.renderEngine.bindTexture(mc.renderEngine.getResourceLocation(0));
                drawTexturedModelRectFromIcon(xOffset, yOffset - textureHeight - verticalOffset, icon, tankWidth, textureHeight);
                verticalOffset = verticalOffset + 16;
            }
            
            changeTexture(getGuiTexture());
            drawTexturedModalRect(xOffset, yOffset - tankHeight, tankX, tankY, tankWidth, tankHeight);
        }
    }

    private void drawTexturedModelRectFromIcon(int x, int y, Icon icon, int width, int height) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMinV());
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMinV());
        tessellator.draw();
    }

}
