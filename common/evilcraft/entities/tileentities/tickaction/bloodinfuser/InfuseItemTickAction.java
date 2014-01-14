package evilcraft.entities.tileentities.tickaction.bloodinfuser;

import net.minecraft.item.ItemStack;
import evilcraft.CustomRecipe;
import evilcraft.CustomRecipeRegistry;
import evilcraft.CustomRecipeResult;
import evilcraft.blocks.BloodInfuser;
import evilcraft.entities.tileentities.IConsumeProduceEmptyInTankTile;

public class InfuseItemTickAction extends BloodInfuserTickAction{

    @Override
    public void onTick(IConsumeProduceEmptyInTankTile tile, int tick) {
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
    
    private CustomRecipeResult getResult(IConsumeProduceEmptyInTankTile tile) {
        ItemStack infuseStack = getInfuseStack(tile);
        CustomRecipe customRecipeKey = new CustomRecipe(infuseStack, tile.getTank().getFluid(), BloodInfuser.getInstance());
        CustomRecipeResult result = CustomRecipeRegistry.get(customRecipeKey);
        return result;
    }
    
    @Override
    public int getRequiredTicks(IConsumeProduceEmptyInTankTile tile) {
        CustomRecipeResult result = getResult(tile);
        return getRequiredTicks(tile, result.getRecipe());
    }
    
    private int getRequiredTicks(IConsumeProduceEmptyInTankTile tile, CustomRecipe customRecipe) {
        return customRecipe.getDuration();
    }
    
    @Override
    public int willProduceItemID(IConsumeProduceEmptyInTankTile tile) {
        CustomRecipeResult result = getResult(tile);
        return result.getResult().itemID;
    }
    
    @Override
    public boolean canTick(IConsumeProduceEmptyInTankTile tile, int tick) {
        return getResult(tile) != null && super.canTick(tile, tick);
    }
    
}
