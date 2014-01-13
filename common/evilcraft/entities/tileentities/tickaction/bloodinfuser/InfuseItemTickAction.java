package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import evilcraft.CustomRecipe;
import evilcraft.CustomRecipeRegistry;
import evilcraft.CustomRecipeResult;
import evilcraft.blocks.BloodInfuser;
import evilcraft.entities.tileentities.IConsumeProduceWithTankTile;

public class InfuseItemTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(IConsumeProduceWithTankTile tile, int tick) {
        ItemStack infuseStack = getInfuseStack(tile);
        CustomRecipe customRecipeKey = new CustomRecipe(infuseStack, tile.getTank().getFluid(), BloodInfuser.getInstance());
        CustomRecipeResult result = CustomRecipeRegistry.get(customRecipeKey);
        if(tick >= getRequiredTicks(tile, customRecipeKey)) {
            // TODO: recipe manager for blood infusings
            //System.out.println("found:"+result);
            if(result != null) {
                tile.getInventory().setInventorySlotContents(tile.getProduceSlot(), result.getResult().copy());
                if(result.getRecipe().getFluidStack() != null) {
                    tile.getTank().drain(result.getRecipe().getFluidStack().amount, true);
                }
            }
        }
    }
    
    private int getRequiredTicks(IConsumeProduceWithTankTile tile, CustomRecipe customRecipe) {
        return customRecipe.getDuration();
    }
    
}
