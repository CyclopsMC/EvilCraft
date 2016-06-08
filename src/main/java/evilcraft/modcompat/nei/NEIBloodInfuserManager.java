package evilcraft.modcompat.nei;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.ItemList;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.google.common.collect.Lists;
import evilcraft.Reference;
import evilcraft.api.recipes.custom.IRecipe;
import evilcraft.block.BloodInfuser;
import evilcraft.block.BloodInfuserConfig;
import evilcraft.client.gui.container.GuiBloodInfuser;
import evilcraft.core.client.gui.container.GuiWorking;
import evilcraft.core.fluid.BloodFluidConverter;
import evilcraft.core.recipe.custom.DurationXpRecipeProperties;
import evilcraft.core.recipe.custom.ItemFluidStackAndTierRecipeComponent;
import evilcraft.core.recipe.custom.ItemStackRecipeComponent;
import evilcraft.fluid.Blood;
import evilcraft.inventory.container.ContainerBloodInfuser;
import evilcraft.item.Promise;
import evilcraft.tileentity.TileBloodInfuser;
import evilcraft.tileentity.TileWorking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

/**
 * Manager for the recipes in the {@link BloodInfuser}.
 * @author rubensworks
 *
 */
public class NEIBloodInfuserManager extends TemplateRecipeHandler {
    
    protected static int xOffset = -5;
    protected static int yOffset = -16;
    
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

    private static final int FLUID_CONTAINER_X = ContainerBloodInfuser.SLOT_CONTAINER_X;
    private static final int FLUID_CONTAINER_Y = ContainerBloodInfuser.SLOT_CONTAINER_Y;
    
    private float zLevel = 200.0F;

    public static ArrayList<FluidPair> afluids;
    
    protected class CachedBloodInfuserRecipe extends CachedRecipe {
        
        private int hashcode;
        private List<PositionedStack> input;
        private PositionedStack output;
        private PositionedStack upgrade = null;
        private FluidStack fluidStack;
        private Rectangle tank;
        private int duration;
        private int tier;

        public CachedBloodInfuserRecipe(IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe) {
            this(recipe.getInput().getItemStacks(), recipe.getOutput().getItemStack(),
                    recipe.getInput().getFluidStack(), recipe.getProperties().getDuration(), recipe.getInput().getTier());
        }
        
        public CachedBloodInfuserRecipe(List<ItemStack> inputStacks, ItemStack outputStack, FluidStack fluidStack, int duration, int tier) {
            this.input = Lists.newArrayListWithCapacity(inputStacks.size());
            for(ItemStack itemStack : inputStacks) {
                input.add(new PositionedStack(
                        inputStacks,
                        ContainerBloodInfuser.SLOT_INFUSE_X + xOffset,
                        ContainerBloodInfuser.SLOT_INFUSE_Y + yOffset
                ));
            }
            this.output =
                    new PositionedStack(
                            outputStack,
                            ContainerBloodInfuser.SLOT_INFUSE_RESULT_X + xOffset,
                            ContainerBloodInfuser.SLOT_INFUSE_RESULT_Y + yOffset
                            );
            this.tier = tier;
            if(tier > 0) {
                this.upgrade = new PositionedStack(new ItemStack(Promise.getInstance(), 1, tier - 1), 3, 0);
            }
            this.fluidStack = fluidStack;
            if(this.fluidStack != null) {
                tank = new Rectangle(tankTargetX, -1, tankWidth, tankHeight);
            }
            this.duration = duration;
            calculateHashcode();
        }
        
        private void calculateHashcode() {
            hashcode = input.hashCode() << 16 + output.item.getItemDamage();
            hashcode = 31 * hashcode + (input.hashCode() << 16 + output.item.hashCode());
        }
        
        @Override
        public boolean equals(Object obj)  {
            if(!(obj instanceof CachedBloodInfuserRecipe))
                return false;
            CachedBloodInfuserRecipe recipe2 = (CachedBloodInfuserRecipe)obj;
            if(input.size() != recipe2.input.size()) return false;
            for(int i = 0; i < input.size(); i++) {
                if(!NEIServerUtils.areStacksSameType(input.get(i).item, recipe2.input.get(i).item)) return false;
            }
            return output.item.getItemDamage() == recipe2.output.item.getItemDamage();
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
            return input.get(0);
        }

        @Override
        public PositionedStack getOtherStack() {
            return afluids.get((cycleticks / 24) % afluids.size()).stack;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            List<PositionedStack> ingredients = getCycledIngredients(cycleticks / 32, input);
            if(upgrade != null) ingredients.add(upgrade);
            return ingredients;
        }
        
    }

    public static class FluidPair {
        public FluidPair(ItemStack itemStack, FluidStack fluidStack) {
            this.stack = new PositionedStack(itemStack, FLUID_CONTAINER_X + xOffset, FLUID_CONTAINER_Y + yOffset, false);
            this.fluidStack = fluidStack;
        }

        public PositionedStack stack;
        public FluidStack fluidStack;
    }

    public NEIBloodInfuserManager() {
        LinkedList<RecipeTransferRect> guiTransferRects = new LinkedList<RecipeTransferRect>();
        guiTransferRects.add(
                new RecipeTransferRect(
                        new Rectangle(
                                GuiBloodInfuser.PROGRESSTARGETX + GuiWorking.UPGRADES_OFFSET_X - 6,
                                GuiBloodInfuser.PROGRESSTARGETY - 10,
                                progressWidth, progressHeight
                        ),
                        getBIOverlayIdentifier()
                )
        );

        LinkedList<Class<? extends GuiContainer>> list = new LinkedList<Class<? extends GuiContainer>>();
        list.add(getGuiClass());
        RecipeTransferRectHandler.registerRectsToGuis(list, guiTransferRects);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        if(afluids == null || afluids.isEmpty())
            findFluids();
        return super.newInstance();
    }

    private static void findFluids() {
        afluids = new ArrayList<FluidPair>();

        for (FluidContainerRegistry.FluidContainerData fluidContainerData :
                FluidContainerRegistry.getRegisteredFluidContainerData()) {
            if(BloodFluidConverter.getInstance().canConvert(fluidContainerData.fluid.getFluid())) {
                afluids.add(new FluidPair(fluidContainerData.filledContainer.copy(), fluidContainerData.fluid));
            }
        }

        for (ItemStack item : ItemList.items) {
            if(item.getItem() instanceof IFluidContainerItem) {
                IFluidContainerItem containerItem = (IFluidContainerItem) item.getItem();
                try {
                    FluidStack fluidStack = containerItem.getFluid(item);
                    if (fluidStack != null && BloodFluidConverter.getInstance().canConvert(fluidStack.getFluid())) {
                        afluids.add(new FluidPair(item.copy(), fluidStack));
                    }
                } catch (Exception e) {
                    // Just ignore that fluid
                }
            }
        }
    }
    
    @Override
    public void loadTransferRects() {
        transferRects.clear();
        transferRects.add(
                new RecipeTransferRect(
                    new Rectangle(
                            GuiBloodInfuser.PROGRESSTARGETX - 5,
                            GuiBloodInfuser.PROGRESSTARGETY - 15,
                            progressWidth, progressHeight
                            ),
                    getBIOverlayIdentifier()
                )
        );
        transferRects.add(
                new RecipeTransferRect(
                        new Rectangle(
                                GuiBloodInfuser.TANKTARGETX - 5,
                                GuiBloodInfuser.TANKTARGETY - tankHeight - 15,
                                tankWidth, tankHeight
                        ),
                    getFluidOverlayIdentifier(),
                    new FluidStack(Blood.getInstance(), FluidContainerRegistry.BUCKET_VOLUME)
                )
        );
    }

    @Override
    public List<Class<? extends GuiContainer>> getRecipeTransferRectGuis() {
        return null; // We will do transfer rect registering ourselves.
    }

    private String getBIOverlayIdentifier() {
        return BloodInfuserConfig._instance.getNamedId();
    }

    protected String getFluidOverlayIdentifier() {
        return "liquid";
    }
    
    @Override
    public String getOverlayIdentifier() {
        return getBIOverlayIdentifier();
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiBloodInfuser.class;
    }

    @Override
    public String getRecipeName() {
        return BloodInfuser.getInstance().getLocalizedName();
    }
    
    private List<CachedBloodInfuserRecipe> getRecipes() {
        List<CachedBloodInfuserRecipe> recipes = new LinkedList<CachedBloodInfuserRecipe>();

        for (IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe :
        	BloodInfuser.getInstance().getRecipeRegistry().allRecipes())
            recipes.add(new CachedBloodInfuserRecipe(recipe));

        return recipes;
    }
    
    @Override
    public int recipiesPerPage() {
        return 2;
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if(outputId.equals(getBIOverlayIdentifier())) {
            for(CachedBloodInfuserRecipe recipe : getRecipes()) {
                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        List<IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties>> recipes =
                BloodInfuser.getInstance().getRecipeRegistry().findRecipesByOutput(new ItemStackRecipeComponent(result));

        for(IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe : recipes) {
            arecipes.add(new CachedBloodInfuserRecipe(recipe));
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
    	if(TileBloodInfuser.ACCEPTED_FLUID != null) {
    		try {
		        IRecipe<ItemFluidStackAndTierRecipeComponent, ItemStackRecipeComponent, DurationXpRecipeProperties> recipe = BloodInfuser
		        		.getInstance().getRecipeRegistry().findRecipeByInput(
		                new ItemFluidStackAndTierRecipeComponent(
		                        ingredient,
		                        new FluidStack(TileBloodInfuser.ACCEPTED_FLUID, TileBloodInfuser.LIQUID_PER_SLOT), -1)
		        );
		
		        if(recipe != null) {
		            arecipes.add(new CachedBloodInfuserRecipe(recipe));
		        }
    		} catch (NullPointerException e) {
    			// In this case, the fluid was somehow incorrectly registered.
    		}
    	}
    }

    @Override
    public String getGuiTexture() {
        return Reference.MOD_ID + ":" + BloodInfuser.getInstance().getGuiTexture("_nei");
    }
    
    protected CachedBloodInfuserRecipe getRecipe(int recipe) {
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
        
        CachedBloodInfuserRecipe bloodInfuserRecipe = getRecipe(recipe);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int tankSize = bloodInfuserRecipe.fluidStack.amount * tankHeight / getMaxTankSize(bloodInfuserRecipe);
        drawTank(tankTargetX, tankTargetY, TileBloodInfuser.ACCEPTED_FLUID.getID(), tankSize);
        GL11.glDisable(GL11.GL_BLEND);
    }

    protected int getMaxTankSize(CachedBloodInfuserRecipe bloodInfuserRecipe) {
        return TileBloodInfuser.LIQUID_PER_SLOT * TileWorking.getTankTierMultiplier(bloodInfuserRecipe.tier);
    }
    
    protected void drawTank(int xOffset, int yOffset, int fluidID, int level) {
        Minecraft mc = Minecraft.getMinecraft();
        FluidStack stack = new FluidStack(fluidID, 1);
        if(fluidID > 0) {
            IIcon icon = stack.getFluid().getIcon();
            if (icon == null) icon = Blocks.water.getIcon(0, 0);
            
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
                
                mc.renderEngine.bindTexture(mc.renderEngine.getResourceLocation(0));
                drawTexturedModelRectFromIcon(xOffset, yOffset - textureHeight - verticalOffset, icon, tankWidth, textureHeight);
                verticalOffset = verticalOffset + 16;
            }
            
            changeTexture(getGuiTexture());
            drawTexturedModalRect(xOffset, yOffset - tankHeight, tankX, tankY, tankWidth, tankHeight);
        }
    }

    private void drawTexturedModelRectFromIcon(int x, int y, IIcon icon, int width, int height) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMaxV());
        tessellator.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)icon.getMaxU(), (double)icon.getMinV());
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)icon.getMinU(), (double)icon.getMinV());
        tessellator.draw();
    }

    @Override
    public List<String> handleTooltip(GuiRecipe guiRecipe, List<String> currenttip, int recipe) {
        super.handleTooltip(guiRecipe, currenttip, recipe);
        CachedBloodInfuserRecipe bloodInfuserRecipe = getRecipe(recipe);
        FluidStack fluid = bloodInfuserRecipe.fluidStack;
        if(fluid != null) {
            Point mouse = GuiDraw.getMousePosition();
            Point offset = guiRecipe.getRecipePosition(recipe);
            Point mouseRelative = new Point(mouse.x - ((guiRecipe.width - width) / 2) - offset.x,
                    mouse.y - ((guiRecipe.height - height) / 2) - offset.y);
            if (bloodInfuserRecipe.tank.contains(mouseRelative)) {
                currenttip.add(fluid.getLocalizedName());
                currenttip.add(fluid.amount + " / " + getMaxTankSize(bloodInfuserRecipe) + " mB");
            }
        }
        return currenttip;
    }

}
