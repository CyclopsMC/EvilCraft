package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import evilcraft.CustomRecipe;
import evilcraft.CustomRecipeRegistry;
import evilcraft.CustomRecipeResult;
import evilcraft.blocks.BloodInfuser;
import evilcraft.entities.tileentities.IConsumeProduceWithTankTile;
import evilcraft.items.BucketBloodConfig;

public class InfuseItemTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(IConsumeProduceWithTankTile tile, int tick) {
        CustomRecipeResult result = getResult(tile);
        if(tick >= getRequiredTicks(tile, result.getRecipe())) {
            if(result != null) {
                if(addToProduceSlot(tile, result.getResult().copy())) {
                    tile.getInventory().decrStackSize(tile.getConsumeSlot(), 1);
                    tile.getTank().drain(result.getRecipe().getFluidStack().amount, true);
                }
            }
        }
    }
    
    private CustomRecipeResult getResult(IConsumeProduceWithTankTile tile) {
        ItemStack infuseStack = getInfuseStack(tile);
        CustomRecipe customRecipeKey = new CustomRecipe(infuseStack, tile.getTank().getFluid(), BloodInfuser.getInstance());
        CustomRecipeResult result = CustomRecipeRegistry.get(customRecipeKey);
        return result;
    }
    
    @Override
    public int getRequiredTicks(IConsumeProduceWithTankTile tile) {
        CustomRecipeResult result = getResult(tile);
        return getRequiredTicks(tile, result.getRecipe());
    }
    
    private int getRequiredTicks(IConsumeProduceWithTankTile tile, CustomRecipe customRecipe) {
        return customRecipe.getDuration();
    }
    
    @Override
    public int willProduceItemID(IConsumeProduceWithTankTile tile) {
        CustomRecipeResult result = getResult(tile);
        return result.getResult().itemID;
    }
    
}
